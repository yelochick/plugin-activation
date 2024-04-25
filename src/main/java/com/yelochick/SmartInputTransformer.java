package com.yelochick;

import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

/**
 * @author yelochick
 */
public class SmartInputTransformer implements MyTransformer {
    
    @Override
    public String getHookClassName() {
        return "com/xxxtai/smartinputintellij/model/LicenseInfo";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("enableLicense".equals(m.name)) {
                InsnList list = new InsnList();
                list.add(new LdcInsnNode(""));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                LabelNode labelNode = new LabelNode();
                list.add(new JumpInsnNode(IFNE, labelNode));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new FieldInsnNode(PUTFIELD, "com/xxxtai/smartinputintellij/model/LicenseInfo", "license", "Ljava/lang/String;"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new LdcInsnNode(4102415999000L));
                list.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false));
                list.add(new FieldInsnNode(PUTFIELD, "com/xxxtai/smartinputintellij/model/LicenseInfo", "expireTimestamp", "Ljava/lang/Long;"));
                list.add(new InsnNode(ICONST_1));
                list.add(new InsnNode(IRETURN));
                list.add(labelNode);
                m.instructions.insert(list);
            } else if ("isOffline".equals(m.name)) {
                m.instructions.clear();
                InsnList list = new InsnList();
                list.add(new InsnNode(ICONST_1));
                list.add(new InsnNode(IRETURN));
                m.instructions.insert(list);
            }
        }
        SafeClassWriter writer = new SafeClassWriter(null, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
