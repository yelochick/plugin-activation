package com.yelochick.coolrequest;

import com.janetfilter.core.plugin.MyTransformer;
import com.yelochick.SafeClassWriter;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

/**
 * @author yelochick
 */
public class VipApiTransformer implements MyTransformer {

    public static final String LICENSE = "AAAAAAAAAAlDTE9VRF9BUEkAAAFYc0hvbGk5aXQ1M1lGSXFjc0puUG5OMm1GekdPQzZ4Qi9sS1BsMDdlL3RadkRuVlBNa1J1eWJKY1ZOU0Flb1QvbTRPM0VlNmxaZVhLdHNwUE5uRFc3YldodzBHdFRoUTlHemlYN3FSRFN6ek0wS3pYYm91YnBFVjgrVjVIR210MXlaMmZHdXdSaWdlRkFhSE5ObEg1T3VxMS9PMFdMK0pZdmNJTU5PdjdSSE1QeDRkdXFaanZUdk9KRFlmMldkc2QyRnZPWkExZlBlK2pWeStQN00xM0lJNklrQXN5RHJWU0RzMWlpSTUvVFZLN0FzMGU5Qmk0TWc0VFNiMmhoT3RHWFN6d1lpT1BsVWl1WC8xbFZyUGw0alU0VkhOb2VIVWF3TXBiNk9BaWgyUTZadmRpV2swWE1zM21vOCtSeWlZcWNvQVNjditGcjE1TnA1Y2YvUEVOWXJRPT0AAAAIRkVBVFVSRVMAAAFYUHRiU2tHUEZZZDZOMHhGQThBUTlwSXNVcG1iVkdFT1ZoWklJRFo3dDhwdkM2RmZFbCtpbUJTNERKR2Y0KytZMURhbFpBR0JrWnZrcnB5VkJSWXhSWFNRTWpaVVMvZGZlQzNza3lzRVZ6OC9VUW1wWEhIOHZVWFM2NVFVdkU4LzdYeGp0ODExdWsxeDdNZHFnYWZPb0FidDdqSjVxTkcrbnZyYldUdTRFbXlMMS8wZS84NTRidTBWdHlJMzNSWWtsaEs5bGRNcjZDNExQTHBUWVRlMTVRZkFqRU5jblphRVpFRTBpQUhXVE5aemJkZWZ0UElTdDFtSGFzR0IycTRPSm1JUFVSR3Irb0JzY0tDcmJqZ0Z5QUM3Mk4rcnFQbTlPNmtQem5oU2wyenYvNjBNTjBvaW1IdTRLbkZRcVB0SEd4UFRzMUI4cS9FU2hybktzZ0R3ZnV3PT0=";
    public static final  String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDXZ4VpP4EkzR7XtZ3s6HulG8fXEris5GPWzRnFLh0ae2EUNROndTc5RutNbPMNf6PmPrg3pTKb3zAmleS3hGE7IrxoDPLDdS619CabEhVEqwg0CbJwtNTH+91kaU/p5fziNhtr7p2KWaEGoN/OYsQy+fdlFi1qJgrxXMfaTsnqgdYfkZtsdjlgrdImAQAZSGV12SrbKeH8Y1zBDxhwsBXFTOMFTpyF+QfGG/AL+DrVrmQ6vmXHUdpgYBEq82JTDxkja7W0z53WFSKb2b2E0su58I4K6fmNydeXw0/e7h+QWv0sMl/JYr+g/MOcFm/MR4wb4FBYa6k3LqD0H0oJbBlrAgMBAAECggEAB6MEcFhPiFH7Y3Ntb25In/Bts9rWkE6imYV5AewiHQO9y90b3saBmDbC9eJEdbiOQ4X6uwJQ2oTdrKiIzNbVKacQHfJDkxlu+AV5busDtQUJS+sCGAvZNWPhFAPF0QmV3yFC9J8nl7WYx2bhuGv4/8Or+ToBjy9dGPSMo1lCwF0cX08FgOHKa/chS09nDK/C3WHYZH5dqXufR4zYfh5byX0sDrBxANQuKVdKj++PrgleOk0C6LV3ax2DcG1udCwSxJ4kHDZI1PoEnFtirXYOTyKjok9L5ZgjijugPQ0Nreg9SYo+ETFnbaK8rU55FTcMJt3wquuJm5h2IX/LfwGVSQKBgQD5g8sh/AMQXgnHnAbSysU5sRWpyeDUkCVC3BpDeaBbO6J4x0FrjzTeqe7UGfq54pxjZS7qfFtlm3lGt8m3ZHdnTqujYwDo9PQYKjozpftUU4qZ/oW15K2Rsl1YTpEwkQM4hSEvmzIn0P2+ZLcf7ybJuB6NLf1RVjbzVf6GDxilAwKBgQDdAMQEHBNhq0Nw5sfzc/50hpmNCiZ+ia8dkPyKKGH8xZt7D1X/hOBeDG0F/qWtue/76aRIQYBGiktEiMWje8iQwaTxSDvHTjjwnugykiGyb0X4yq2Co8NJ/KCyBY+XImN7E92ArErbdTDXfMSwP9VpbgxnkVCzwMmnV4L9g1MJeQKBgDEwgkNY42G+qD7py46S2v7comCuo9xD7pSXv7W9rJdP0l/tKECEIvAioCrAVGWA/0O5Ft6zVXFajTYb1XCKJd//6mZzhiJm6Bg7eu3jswCU16Y0Pzf0tSMb/rsCAi2I6RGjbT9tYR3oXZ3DTTbGducdnShIAMCgNoUSVvMm1rzxAoGBAJfRt9ZO5y2YNlsv3roqu+mWsZNHHsycjvTTrfjG5xF7OmUeUuiWgn95L8gHeA9RBUBb76RHgvRyemRZRNQa69wter7Z7V7sKId70m0zH/zxHfNcYDe3gieoaUgCM/MSB578NZT4RvBdoDDYIFInd9bWZ6UaGYNyc9UGdBDtjzSZAoGASMb03o9i1ntzVACI2JCva/wpTNx34uWdOHpJ8eTO3euCbhrY4VUjhmq4E0O0j3hjnNdtWOrjGU3dKvAQPhkTYq7HQuqd7cgzGjz769P6Baa/zNHvmpU/3hczY7chdl5e2VkZZ0TPnNFERzV1EpJVbFcSMxrBG6AVeN2LejTrO4U=";


    @Override
    public String getHookClassName() {
        return "com/cool/request/vip/VipApi";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);

        for (MethodNode m : node.methods) {
            if ("getUserInfo".equals(m.name)) {
                m.instructions.clear();
                InsnList list = new InsnList();
                list.add(new TypeInsnNode(NEW, "com/cool/request/vip/UserInfo"));
                list.add(new InsnNode(DUP));
                list.add(new MethodInsnNode(INVOKESPECIAL, "com/cool/request/vip/UserInfo", "<init>", "()V", false));
                list.add(new VarInsnNode(ASTORE, 1));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new InsnNode(ICONST_0));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/vip/UserInfo", "setCode", "(I)V", false));
                list.add(new TypeInsnNode(NEW, "com/cool/request/vip/UserInfo$Info"));
                list.add(new InsnNode(DUP));
                list.add(new MethodInsnNode(INVOKESPECIAL, "com/cool/request/vip/UserInfo$Info", "<init>", "()V", false));
                list.add(new VarInsnNode(ASTORE, 2));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new LdcInsnNode(LICENSE));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/vip/UserInfo$Info", "setLicense", "(Ljava/lang/String;)V", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new LdcInsnNode("YeloChick"));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/vip/UserInfo$Info", "setName", "(Ljava/lang/String;)V", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new LdcInsnNode(PRIVATE_KEY));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/vip/UserInfo$Info", "setPrivateKey", "(Ljava/lang/String;)V", false));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/vip/UserInfo", "setData", "(Lcom/cool/request/vip/UserInfo$Info;)V", false));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new InsnNode(ARETURN));
                m.instructions.insert(list);
            } else if ("checkToken".equals(m.name)) {
                m.instructions.clear();
                InsnList list = new InsnList();
                list.add(new TypeInsnNode(NEW, "com/cool/request/idea/TokenInfo"));
                list.add(new InsnNode(DUP));
                list.add(new MethodInsnNode(INVOKESPECIAL, "com/cool/request/idea/TokenInfo", "<init>", "()V", false));
                list.add(new VarInsnNode(ASTORE, 2));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new InsnNode(ICONST_0));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/idea/TokenInfo", "setCode", "(I)V", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new LdcInsnNode("1"));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/idea/TokenInfo", "setMsg", "(Ljava/lang/String;)V", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/cool/request/idea/TokenInfo", "setData", "(Ljava/lang/String;)V", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new InsnNode(ARETURN));
                m.instructions.insert(list);
            }
        }

        SafeClassWriter writer = new SafeClassWriter(null, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
