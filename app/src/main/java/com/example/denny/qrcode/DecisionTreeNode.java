package com.example.denny.qrcode;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zz on 4/8/17.
 */

public class DecisionTreeNode {
    private DecisionTreeNode parent;
    private DecisionTreeNode trueChild;
    private DecisionTreeNode falseChild;


    private static final int ATTR_SPACE = 0;
    private static final int ATTR_HTTP = 1;
    private static final int ATTR_WWW = 2;
    private static final int ATTR_TLD = 3;
    private static final int ATTR_NAME = 4;
    private static final int ATTR_ADDR = 5;
    private static final int ATTR_TEL = 6;
    private static final int ATTR_EMAIL = 7;
    private static final int ATTR_MATMSG = 8;
    private static final int ATTR_BODY = 9;
    private static final int ATTR_SUB = 10;

//    private static final int[] attributes = {ATTR_SPACE, ATTR_HTTP, ATTR_WWW, ATTR_TLD, ATTR_NAME, ATTR_ADDR, ATTR_TEL, ATTR_EMAIL, ATTR_MATMSG, ATTR_BODY, ATTR_SUB};

    private static final String[] attributes = {" ", "https?://", "www\\.", "(\\.com)|(\\.org)|(\\.gov)|(\\.net)|(\\.edu)",
            "MATMSG", "SUB:.*;", "BODY:.*;",
            "N:.*;", "ADR:.*;", "TEL:.*;", "EMAIL:.*;"};

    private Pattern pattern;
    private int numTEXT;
    private int numURL;
    private int numEMAIL;
    private int numCONTACT;

    public DecisionTreeNode(String attribute, int numTEXT, int numURL, int numEMAIL, int numCONTACT) {
        this.pattern = attribute == null ?  null : Pattern.compile(attribute);
        this.numTEXT = numTEXT;
        this.numURL = numURL;
        this.numEMAIL = numEMAIL;
        this.numCONTACT = numCONTACT;
        this.parent = null;
        this.trueChild = null;
        this.falseChild = null;
    }

    public void setParent(DecisionTreeNode parent) {
        this.parent = parent;
    }

    public void setChildren(DecisionTreeNode trueChild, DecisionTreeNode falseChild) {
        this.trueChild = trueChild;
        this.falseChild = falseChild;
    }

    public static DecisionTreeNode learn(boolean[][] features, String[] outputs, String[] remainAttributes) {
        int numTEXT = 0;
        int numURL = 0;
        int numCONTACT = 0;
        int numEMAIL = 0;
        int total = features.length;

        if(remainAttributes.length == 0) {
            for(int i = 0; i < total; i++) {
                if(outputs[i].equals("TEXT")) {
                    numTEXT++;
                } else if(outputs[i].equals("URL")) {
                    numURL++;
                } else if(outputs[i].equals("CONTACT")) {
                    numCONTACT++;
                } else {
                    numEMAIL++;
                }

            }
            return new DecisionTreeNode(null, numTEXT, numURL, numEMAIL, numCONTACT);
        }

        int bestAttr = 0;
        double maxInfoGain = 0.0;
        for(int i = 0; i < remainAttributes.length; i++) {
            double infoGain = DecisionTreeNode.infoGain(i, features, outputs);
            bestAttr = maxInfoGain <= infoGain ? i : bestAttr;
            maxInfoGain = maxInfoGain <= infoGain ? infoGain : maxInfoGain;
        }

        List<boolean[]> trueInputs = new ArrayList<boolean[]>();
        List<String> trueOutputs = new ArrayList<String>();
        List<boolean[]> falseInputs = new ArrayList<boolean[]>();
        List<String> falseOutputs = new ArrayList<String>();

        for(int i = 0; i < total; i++) {
            if(outputs[i].equals("TEXT")) {
                numTEXT++;
            } else if(outputs[i].equals("URL")) {
                numURL++;
            } else if(outputs[i].equals("CONTACT")) {
                numCONTACT++;
            } else {
                numEMAIL++;
            }

            if(features[i][bestAttr]) {
                trueInputs.add(features[i]);
                trueOutputs.add(outputs[i]);
            } else {
                falseInputs.add(features[i]);
                falseOutputs.add(outputs[i]);
            }

        }

        List<String> newAttributes = new ArrayList<String>();
        newAttributes.addAll(Arrays.asList(remainAttributes));
        newAttributes.remove(bestAttr);

        DecisionTreeNode node = new DecisionTreeNode(DecisionTreeNode.attributes[bestAttr], numTEXT, numURL, numEMAIL, numCONTACT);
//
//        System.out.println("--------------------------------------");
//        System.out.println("numText: " + numTEXT);
//        System.out.println("numURL: " + numURL);
//        System.out.println("numEMAIL: " + numEMAIL);
//        System.out.println("numCONTACT: " + numCONTACT);
//        System.out.println("Attribute: " + attributes[bestAttr]);
//        System.out.println("--------------------------------------\n\n\n\n\n");

        int trueSize = trueInputs.size();
        int falseSize = falseInputs.size();
        DecisionTreeNode trueChild = DecisionTreeNode.learn(trueInputs.toArray(new boolean[trueSize][trueSize > 0 ? trueInputs.get(0).length : 0]), trueOutputs.toArray(new String[trueSize]), newAttributes.toArray(new String[newAttributes.size()]));
        DecisionTreeNode falseChild = DecisionTreeNode.learn(falseInputs.toArray(new boolean[falseSize][falseSize > 0 ? falseInputs.get(0).length : 0]), falseOutputs.toArray(new String[falseSize]), newAttributes.toArray(new String[newAttributes.size()]));
        node.setChildren(trueChild, falseChild);
        trueChild.setParent(node);
        falseChild.setParent(node);
        return node;
    }

    private static double infoGain(int attributeIndex, boolean[][] features, String[] outputs) {
        int numTEXT = 0;
        int numURL = 0;
        int numCONTACT = 0;
        int numEMAIL = 0;

        int numTrueTEXT = 0;
        int numFalseTEXT = 0;

        int numTrueURL = 0;
        int numFalseURL = 0;

        int numTrueCONTACT = 0;
        int numFalseCONTACT = 0;

        int numTrueEMAIL = 0;
        int numFalseEMAIL = 0;

        int total = features.length;

        for(int i = 0; i < total; i++) {
            if(outputs[i].equals("TEXT")) {
                numTEXT++;
                if(features[i][attributeIndex]) {
                    numTrueTEXT++;
                } else {
                    numFalseTEXT++;
                }
            } else if(outputs[i].equals("URL")) {
                numURL++;
                if(features[i][attributeIndex]) {
                    numTrueURL++;
                } else {
                    numFalseURL++;
                }
            } else if(outputs[i].equals("CONTACT")) {
                numCONTACT++;
                if(features[i][attributeIndex]) {
                    numTrueCONTACT++;
                } else {
                    numFalseCONTACT++;
                }
            } else {
                numEMAIL++;
                if(features[i][attributeIndex]) {
                    numTrueEMAIL++;
                } else {
                    numFalseEMAIL++;
                }
            }
        }

        int numTrue = numTrueCONTACT + numTrueEMAIL + numTrueTEXT + numTrueURL;

        double oldEntropy = DecisionTreeNode.entropy(numTEXT, numURL, numCONTACT, numEMAIL);
        double remainder = (double) numTrue / total * DecisionTreeNode.entropy(numTrueTEXT, numTrueURL, numTrueCONTACT, numTrueEMAIL) +
                (1 - (double) numTrue / total) * DecisionTreeNode.entropy(numFalseTEXT, numFalseURL, numFalseCONTACT, numFalseEMAIL);
        return oldEntropy - remainder;
    }


    private static double entropy(int numTEXT, int numURL, int numCONTACT, int numEMAIL) {
        int sum = numCONTACT + numEMAIL + numTEXT + numURL;
        double pTEXT = (double) numTEXT / sum;
        double pURL = (double) numURL / sum;
        double pCONTACT = (double) numCONTACT / sum;
        double pEMAIL = (double) numEMAIL / sum;
        return pTEXT * Math.log(1/pTEXT) / Math.log(2) + pURL * Math.log(1/pTEXT) / Math.log(2) +
                pCONTACT * Math.log(1/pCONTACT) / Math.log(2) + pEMAIL * Math.log(1/pEMAIL) / Math.log(2);
    }

    private int getSum() {
        return this.numTEXT + this.numURL + this.numEMAIL + this.numCONTACT;
    }

    public String getMax() {
        int max = 0;
        int[] list = {this.numTEXT, this.numURL, this.numEMAIL, this.numCONTACT};
        for(int num : list) {
            max = max < num ? num : max;
        }
        if(max == this.numTEXT) {
            return "TEXT";
        } else if(max == this.numURL) {
            return "URL";
        } else if(max == this.numEMAIL) {
            return "EMAIL";
        } else {
            return "CONTACT";
        }
    }

    public String decide(String qrContent) {
        int sum = this.getSum();
        if(sum == 0) {
            if(this.parent == null) {
                return "NO INFO";
            } else {
                return this.parent.getMax();
            }
        } else if(sum == this.numTEXT) {
            return "TEXT";
        } else if(sum == this.numURL) {
            return "URL";
        } else if(sum == this.numEMAIL) {
            return "EMAIL";
        } else if(sum == this.numCONTACT) {
            return "CONTACT";
        } else if(this.pattern == null) {
            return this.getMax();
        } else {
            Matcher matcher = this.pattern.matcher(qrContent);
            if(matcher.find()) {
                return this.trueChild.decide(qrContent);
            } else {
                return this.falseChild.decide(qrContent);
            }
        }
    }
}
