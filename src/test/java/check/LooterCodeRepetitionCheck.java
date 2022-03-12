package check;

import com.alibaba.fastjson.JSON;
import com.nikfce.annotation.LooterCode;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 检验Looter的子类序号不要有重复
 * @author shenzhencheng 2022/3/11
 */
public class LooterCodeRepetitionCheck {

    private static void check() {
        Map<String, Set<Class<?>>> map = new HashMap<>();
        String scanPackage = "com.nikfce.role";
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> skillSet = reflections.getTypesAnnotatedWith(LooterCode.class);
        for (Class<?> clazz : skillSet) {
            if (clazz.isInterface()) {
                continue;
            }
            LooterCode looterCode = clazz.getAnnotation(LooterCode.class);
            String code = looterCode.value();
            map.computeIfAbsent(code, a -> new HashSet<>());
            map.get(code).add(clazz);
        }
        // 输出所有的技能表
        List<String> codeList = new ArrayList<>(map.keySet());
        codeList.sort(String::compareTo);
        for (String code : codeList) {
            System.out.printf("%s: %s%n", code, map.get(code).stream().map(Class::getSimpleName).collect(Collectors.joining(",")));
        }
        // 找到是否有技能重复了
        for (String code : map.keySet()) {
            if (map.get(code).size() > 1) {
                System.err.println("有角色的唯一码重复了!!!请尽快处理,code: " + code);
                System.err.println(JSON.toJSONString(map.get(code)));
            }
        }
    }

    public static void main(String[] args) {
        check();
    }

}
