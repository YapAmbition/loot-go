package com.nikfce.role;

import com.nikfce.config.LootConfig;
import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.io.*;
import java.util.*;

/**
 * 角色配置解析器
 * @author shenzhencheng 2022/3/11
 */
public class LooterConfigParser {

    /**
     * sample.definition作为示例不做解析
     */
    private static final String SAMPLE_NAME = "sample.definition";
    /**
     * 配置文件后缀
     */
    private static final String SUFFIX = ".definition";

    /**
     * 解析所有配置目录下,.definition结尾的配置文件,将它们定义的角色定义都加载到内存
     */
    public static List<LooterDefinition> parseAll() throws IOException {
        List<LooterDefinition> result = new ArrayList<>();
        Set<String> looterCodeSet = new HashSet<>(); // 用来判断文件中定义的角色是否存在角色码不唯一的请看
        String looterDir = LootConfig.getInstance().getLooterDir();
        File file = new File(looterDir);
        if (file.exists() && file.isDirectory()) {
            String[] filenames = file.list();
            if (filenames == null) {
                return result;
            }
            for (String filename : filenames) {
                if (!filename.endsWith(SUFFIX)) {
                    continue;
                }
                if (SAMPLE_NAME.equalsIgnoreCase(filename)) {
                    continue;
                }
                List<String> roleStrList = readConfigFile(String.format("%s/%s", looterDir, filename));
                if (CollectionUtil.isNotEmpty(roleStrList)) {
                    for (String roleStr : roleStrList) {
                        LooterDefinition looterDefinition = packageLooter(roleStr);
                        if (looterCodeSet.contains(looterDefinition.getCode())) {
                            throw new RuntimeException("looter自定义文件中存在重复的唯一码:" + looterDefinition.getCode());
                        }
                        looterCodeSet.add(looterDefinition.getCode());
                        result.add(looterDefinition);
                    }
                }
            }
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

    private static List<String> readConfigFile(String filename) throws IOException {
        File file = new File(filename);
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
