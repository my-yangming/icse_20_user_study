package net.simon987.server.assembly.instruction;

import net.simon987.server.assembly.CPU;
import net.simon987.server.assembly.Instruction;
import net.simon987.server.assembly.Status;
import net.simon987.server.assembly.Target;

public class JoInstruction extends Instruction {

    private static final int OPCODE = 36;

    private CPU cpu;

    public JoInstruction(CPU cpu) {
        super("jo", OPCODE);
        this.cpu = cpu;
    }

    @Override
    public Status execute(Target src, int srcIndex, Status status) {
        if (status.isOverflowFlag()) {
            cpu.setIp((char) src.get(srcIndex));
        }
        return status;
    }

    @Override
    public Status execute(int src, Status status) {
        if (status.isOverflowFlag()) {
            cpu.setIp((char) src);
        }
        return status;
    }
}
