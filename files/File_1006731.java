/*
 * This file is part of ClassGraph.
 *
 * Author: Luke Hutchison
 *
 * Hosted at: https://github.com/classgraph/classgraph
 *
 * --
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Luke Hutchison
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package nonapi.io.github.classgraph.fastzipfilereader;

import java.io.EOFException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nonapi.io.github.classgraph.utils.FileUtils;

/**
 * A class for reading from a ZipFileSlice.
 */
class ZipFileSliceReader implements AutoCloseable {
    /** The zipfile slice. */
    private final ZipFileSlice zipFileSlice;

    /** The chunk cache. */
    private final ByteBuffer[] chunkCache;

    /** A scratch buffer. */
    private final byte[] scratch = new byte[256];

    /**
     * Constructor.
     *
     * @param zipFileSlice
     *            the zipfile slice
     */
    public ZipFileSliceReader(final ZipFileSlice zipFileSlice) {
        this.zipFileSlice = zipFileSlice;
        this.chunkCache = new ByteBuffer[zipFileSlice.physicalZipFile.numMappedByteBuffers];
    }

    /**
     * Get the 2GB chunk of the zipfile with the given chunk index.
     *
     * @param chunkIdx
     *            the chunk index
     * @return the chunk
     * @throws IOException
     *             if an I/O exception occurs.
     * @throws InterruptedException
     *             if the thread was interrupted.
     */
    private ByteBuffer getChunk(final int chunkIdx) throws IOException, InterruptedException {
        ByteBuffer chunk = chunkCache[chunkIdx];
        if (chunk == null) {
            final ByteBuffer byteBufferDup = zipFileSlice.physicalZipFile.getByteBuffer(chunkIdx).duplicate();
            chunk = chunkCache[chunkIdx] = byteBufferDup;
        }
        return chunk;
    }

    /**
     * Copy from an offset within the file into a byte[] array (possibly spanning the boundary between two 2GB
     * chunks).
     *
     * @param off
     *            the offset
     * @param buf
     *            the buffer to copy into
     * @param bufStart
     *            the start index within the buffer
     * @param numBytesToRead
     *            the number of bytes to read
     * @return the number of bytes read
     * @throws IOException
     *             if an I/O exception occurs.
     * @throws InterruptedException
     *             if the thread was interrupted.
     */
    int read(final long off, final byte[] buf, final int bufStart, final int numBytesToRead)
            throws IOException, InterruptedException {
        if (off < 0 || bufStart < 0 || bufStart + numBytesToRead > buf.length) {
            throw new IndexOutOfBoundsException();
        }
        int currBufStart = bufStart;
        int remainingBytesToRead = numBytesToRead;
        int totBytesRead = 0;
        for (long currOff = off; remainingBytesToRead > 0;) {
            // Find the ByteBuffer chunk to read from
            final long currOffAbsolute = zipFileSlice.startOffsetWithinPhysicalZipFile + currOff;
            final int chunkIdx = (int) (currOffAbsolute / FileUtils.MAX_BUFFER_SIZE);
            final ByteBuffer chunk = getChunk(chunkIdx);
            final long chunkStartAbsolute = ((long) chunkIdx) * (long) FileUtils.MAX_BUFFER_SIZE;
            final int startReadPos = (int) (currOffAbsolute - chunkStartAbsolute);

            // Read from current chunk.
            // N.B. the cast to Buffer is necessary, see:
            // https://github.com/plasma-umass/doppio/issues/497#issuecomment-334740243
            // https://github.com/classgraph/classgraph/issues/284#issuecomment-443612800
            // Otherwise compiling in JDK<9 compatibility mode using JDK9+ causes runtime breakage. 
            ((Buffer) chunk).mark();
            ((Buffer) chunk).position(startReadPos);
            final int numBytesRead = Math.min(chunk.remaining(), remainingBytesToRead);
            try {
                chunk.get(buf, currBufStart, numBytesRead);
            } catch (final BufferUnderflowException e) {
                // Should not happen
                throw new EOFException("Unexpected EOF");
            }
            ((Buffer) chunk).reset();

            currOff += numBytesRead;
            currBufStart += numBytesRead;
            totBytesRead += numBytesRead;
            remainingBytesToRead -= numBytesRead;
        }
        return totBytesRead == 0 && numBytesToRead > 0 ? -1 : totBytesRead;
    }

    /**
     * Get a short from a byte array.
     *
     * @param arr
     *            the byte array
     * @param off
     *            the offset to start reading from
     * @return the short
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    static int getShort(final byte[] arr, final long off) throws IndexOutOfBoundsException {
        final int ioff = (int) off;
        if (ioff < 0 || ioff > arr.length - 2) {
            throw new IndexOutOfBoundsException();
        }
        return ((arr[ioff + 1] & 0xff) << 8) | (arr[ioff] & 0xff);
    }

    /**
     * Get a short from the zipfile slice.
     *
     * @param off
     *            the offset to start reading from
     * @return the short
     * @throws IOException
     *             if an I/O exception occurs.
     * @throws InterruptedException
     *             if the thread was interrupted.
     */
    int getShort(final long off) throws IOException, InterruptedException {
        if (off < 0 || off > zipFileSlice.len - 2) {
            throw new IndexOutOfBoundsException();
        }
        if (read(off, scratch, 0, 2) < 2) {
            throw new EOFException("Unexpected EOF");
        }
        return ((scratch[1] & 0xff) << 8) | (scratch[0] & 0xff);
    }

    /**
     * Get an int from a byte array.
     *
     * @param arr
     *            the byte array
     * @param off
     *            the offset to start reading from
     * @return the int
     * @throws IOException
     *             if an I/O exception occurs.
     */
    static int getInt(final byte[] arr, final long off) throws IOException {
        final int ioff = (int) off;
        if (ioff < 0 || ioff > arr.length - 4) {
            throw new IndexOutOfBoundsException();
        }
        return ((arr[ioff + 3] & 0xff) << 24) //
                | ((arr[ioff + 2] & 0xff) << 16) //
                | ((arr[ioff + 1] & 0xff) << 8) //
                | (arr[ioff] & 0xff);
    }

    /**
     * Get an int from the zipfile slice.
     *
     * @param off
     *            the offset to start reading from
     * @return the int
     * @throws IOException
     *             if an I/O exception occurs.
     * @throws InterruptedException
     *             if the thread was interrupted.
     */
    int getInt(final long off) throws IOException, InterruptedException {
        if (off < 0 || off > zipFileSlice.len - 4) {
            throw new IndexOutOfBoundsException();
        }
        if (read(off, scratch, 0, 4) < 4) {
            throw new EOFException("Unexpected EOF");
        }
        return ((scratch[3] & 0xff) << 24) //
                | ((scratch[2] & 0xff) << 16) //
                | ((scratch[1] & 0xff) << 8) //
                | (scratch[0] & 0xff);
    }

    /**
     * Get a long from a byte array.
     *
     * @param arr
     *            the byte array
     * @param off
     *            the offset to start reading from
     * @return the long
     * @throws IOException
     *             if an I/O exception occurs.
     */
    static long getLong(final byte[] arr, final long off) throws IOException {
        final int ioff = (int) off;
        if (ioff < 0 || ioff > arr.length - 8) {
            throw new IndexOutOfBoundsException();
        }
        return ((arr[ioff + 7] & 0xffL) << 56) //
                | ((arr[ioff + 6] & 0xffL) << 48) //
                | ((arr[ioff + 5] & 0xffL) << 40) //
                | ((arr[ioff + 4] & 0xffL) << 32) //
                | ((arr[ioff + 3] & 0xffL) << 24) //
                | ((arr[ioff + 2] & 0xffL) << 16) //
                | ((arr[ioff + 1] & 0xffL) << 8) //
                | (arr[ioff] & 0xffL);
    }

    /**
     * Get a long from the zipfile slice.
     *
     * @param off
     *            the offset to start reading from
     * @return the long
     * @throws IOException
     *             if an I/O exception occurs.
     * @throws InterruptedException
     *             if the thread was interrupted.
     */
    long getLong(final long off) throws IOException, InterruptedException {
        if (off < 0 || off > zipFileSlice.len - 8) {
            throw new IndexOutOfBoundsException();
        }
        if (read(off, scratch, 0, 8) < 8) {
            throw new EOFException("Unexpected EOF");
        }
        return ((scratch[7] & 0xffL) << 56) //
                | ((scratch[6] & 0xffL) << 48) //
                | ((scratch[5] & 0xffL) << 40) //
                | ((scratch[4] & 0xffL) << 32) //
                | ((scratch[3] & 0xffL) << 24) //
                | ((scratch[2] & 0xffL) << 16) //
                | ((scratch[1] & 0xffL) << 8) //
                | (scratch[0] & 0xffL);
    }

    /**
     * Get a string from a byte array.
     *
     * @param arr
     *            the byte array
     * @param off
     *            the offset to start reading from
     * @param lenBytes
     *            the length of the string in bytes
     * @return the string
     * @throws IOException
     *             if an I/O exception occurs.
     */
    static String getString(final byte[] arr, final long off, final int lenBytes) throws IOException {
        final int ioff = (int) off;
        if (ioff < 0 || ioff > arr.length - lenBytes) {
            throw new IndexOutOfBoundsException();
        }
        return new String(arr, ioff, lenBytes, StandardCharsets.UTF_8);
    }

    /**
     * Get a string from the zipfile slice.
     *
     * @param off
     *            the offset to start reading from
     * @param lenBytes
     *            the length of the string in bytes
     * @return the string
     * @throws IOException
     *             if an I/O exception occurs.
     * @throws InterruptedException
     *             if the thread was interrupted.
     */
    String getString(final long off, final int lenBytes) throws IOException, InterruptedException {
        if (off < 0 || off > zipFileSlice.len - lenBytes) {
            throw new IndexOutOfBoundsException();
        }
        final byte[] scratchToUse = lenBytes <= scratch.length ? scratch : new byte[lenBytes];
        if (read(off, scratchToUse, 0, lenBytes) < lenBytes) {
            throw new EOFException("Unexpected EOF");
        }
        // Assume the entry names are encoded in UTF-8 (should be the case for all jars; the only other
        // valid zipfile charset is CP437, which is the same as ASCII for printable high-bit-clear chars)
        return new String(scratchToUse, 0, lenBytes, StandardCharsets.UTF_8);
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        // Drop refs to ByteBuffer chunks so they can be garbage collected
        Arrays.fill(chunkCache, null);
    }
}
