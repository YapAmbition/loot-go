package com.nikfce.role;

import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色配置解析器
 * @author shenzhencheng 2022/3/11
 */
public class LooterConfigParser {

    private static final String ROLE_CONFIG_FILE_PATH = "/Users/shenzhencheng/Documents/github/loot-go/src/main/resources/role.loot";

    public static List<LooterDefinition> parse() throws IOException {
        List<String> roleStrList = readConfigFile();
        if (CollectionUtil.isEmpty(roleStrList)) {
            return new ArrayList<>();
        }
        List<LooterDefinition> result = new ArrayList<>();
        for (String roleStr : roleStrList) {
            LooterDefinition looterDefinition = packageLooter(roleStr);
            result.add(looterDefinition);
        }
        return result;
    }

    private static LooterDefinition packageLooter(String roleStr) {
        String[] items = roleStr.split("\\s+");
        LooterDefinition looterDefinition = new LooterDefinition();
        looterDefinition.setCode(items[0].trim());
        looterDefinition.setName(items[1].trim());
        looterDefinition.setSkillCodeList(Arrays.asList(items[2].trim().split(",")));

        String[] propertyStr = items[3].trim().split(",");
        Properties.PropertiesBuilder propertiesBuilder = Properties.PropertiesBuilder.create();
        for (int i = 0 ; i < propertyStr.length ; i ++) {
            String property = propertyStr[i];
            if (StringUtil.isEmpty(property)) {
                continue;
            }
            switch (i) {
                case 0:
                    propertiesBuilder.setApplyAttribute("1".equals(property));
                    break;
                case 1:
                    propertiesBuilder.setPhysique(Double.parseDouble(property));
                    break;
                case 2:
                    propertiesBuilder.setStrength(Double.parseDouble(property));
                    break;
                case 3:
                    propertiesBuilder.setAgility(Double.parseDouble(property));
                    break;
                case 4:
                    propertiesBuilder.setMaxHp(Double.parseDouble(property));
                    break;
                case 5:
                    propertiesBuilder.setHp(Double.parseDouble(property));
                    break;
                case 6:
                    propertiesBuilder.setAttack(Double.parseDouble(property));
                    break;
                case 7:
                    propertiesBuilder.setDefence(Double.parseDouble(property));
                    break;
                case 8:
                    propertiesBuilder.setStrike(Double.parseDouble(property));
                    break;
                case 9:
                    propertiesBuilder.setSpeed(Double.parseDouble(property));
                    break;
                case 10:
                    propertiesBuilder.setDodge(Double.parseDouble(property));
                    break;
                case 11:
                    propertiesBuilder.setLuck(Double.parseDouble(property));
                    break;
            }
        }
        Properties properties = propertiesBuilder.build();
        looterDefinition.setBasicProperties(properties);
        return looterDefinition;
    }

    private static List<String> readConfigFile() throws IOException {
        File file = new File(ROLE_CONFIG_FILE_PATH);
        if (!file.exists()) {
            return null;
        }
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        List<String> result = new ArrayList<>();
        String tmp ;
        while ((tmp = br.readLine()) != null) {
            if (tmp.trim().length() == 0 || tmp.trim().startsWith("#")) {
                continue;
            }
            result.add(tmp);
        }
        br.close();
        fr.close();
        return result;
    }

}
