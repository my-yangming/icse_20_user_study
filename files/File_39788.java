// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.proxetta.impl;

import jodd.asm7.ClassReader;
import jodd.proxetta.ProxettaException;
import jodd.proxetta.ProxettaFactory;
import jodd.proxetta.ProxettaNames;
import jodd.proxetta.ProxettaUtil;
import jodd.proxetta.ProxyAspect;
import jodd.proxetta.asm.ProxettaWrapperClassBuilder;
import jodd.proxetta.asm.TargetClassInfoReader;
import jodd.proxetta.asm.WorkData;

/**
 * Creates wrapper using ASM library.
 */
public class WrapperProxettaFactory extends ProxettaFactory<WrapperProxettaFactory, WrapperProxetta> {

	private final boolean createTargetInDefaultCtor;

	public WrapperProxettaFactory(final WrapperProxetta wrapperProxetta) {
		super(wrapperProxetta);
		this.createTargetInDefaultCtor = wrapperProxetta.createTargetInDefaultCtor;
	}

	protected Class targetClassOrInterface;
	protected Class targetInterface;
	protected String targetFieldName = ProxettaNames.wrapperTargetFieldName;

	/**
	 * Defines class or interface to wrap.
	 * For setting the interface of the resulting class,
	 * use {@link #setTargetInterface(Class)}.
	 */
	@Override
	public WrapperProxettaFactory setTarget(final Class target) {
		super.setTarget(target);
		this.targetClassOrInterface = target;
		return this;
	}

	/**
	 * Defines the interface of the resulting class.
	 */
	public WrapperProxettaFactory setTargetInterface(final Class targetInterface) {
		if (!targetInterface.isInterface()) {
			throw new ProxettaException("Not an interface: " + targetInterface.getName());
		}
		this.targetInterface = targetInterface;
		return this;
	}

	/**
	 * Defines custom target field name.
	 */
	public WrapperProxettaFactory setTargetFieldName(final String targetFieldName) {
		this.targetFieldName = targetFieldName;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected WorkData process(final ClassReader cr, final TargetClassInfoReader targetClassInfoReader) {
		final ProxettaWrapperClassBuilder pcb =
				new ProxettaWrapperClassBuilder(
						targetClassOrInterface,
						targetInterface,
						targetFieldName,
						destClassWriter,
						proxetta.getAspects(new ProxyAspect[0]),
						resolveClassNameSuffix(),
						requestedProxyClassName,
						targetClassInfoReader,
						createTargetInDefaultCtor
				);

		cr.accept(pcb, 0);

		return pcb.getWorkData();
	}

	/**
	 * Injects target into wrapper.
	 */
	public void injectTargetIntoWrapper(final Object target, final Object wrapper) {
		ProxettaUtil.injectTargetIntoWrapper(target, wrapper, targetFieldName);
	}

}
