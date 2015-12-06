package factorization.servo.instructions;

import factorization.api.Coord;
import factorization.api.FzOrientation;
import factorization.api.datahelpers.DataHelper;
import factorization.api.datahelpers.IDataSerializable;
import factorization.api.datahelpers.Share;
import factorization.servo.AbstractServoMachine;
import factorization.servo.CpuBlocking;
import factorization.servo.Instruction;
import factorization.servo.ServoMotor;
import factorization.servo.stepper.StepperEngine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.io.IOException;

public class Spin extends Instruction {
    boolean cc = true;
    
    @Override
    public IDataSerializable putData(String prefix, DataHelper data) throws IOException {
        cc = data.as(Share.VISIBLE, prefix + "cc").putBoolean(cc);
        return this;
    }
    
    @Override
    public boolean onClick(EntityPlayer player, Coord block, EnumFacing side) {
        if (player.worldObj.isRemote) {
            return true;
        }
        if (playerHasProgrammer(player)) {
            cc = !cc;
            return true;
        }
        return false;
    }

    @Override
    protected ItemStack getRecipeItem() {
        return new ItemStack(Items.string);
    }

    void hit(AbstractServoMachine motor) {
        EnumFacing newTop = motor.getOrientation().top;
        for (int i = cc ? 3 : 1; i > 0; i--) {
            newTop = newTop.getRotation(motor.getOrientation().facing);
        }
        FzOrientation next = motor.getOrientation().pointTopTo(newTop);
        if (next != null) {
            motor.setOrientation(next);
        }
    }

    @Override
    public void motorHit(ServoMotor motor) {
        hit(motor);
    }

    @Override
    public void stepperHit(StepperEngine engine) {
        hit(engine);
    }

    @Override
    public String getName() {
        return "fz.instruction.spin";
    }
    
    @Override
    public CpuBlocking getBlockingBehavior() {
        return CpuBlocking.BLOCK_UNTIL_NEXT_ENTRY;
    }
}
