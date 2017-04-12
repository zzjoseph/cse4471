package com.example.denny.qrcode;

import com.opencsv.CSVReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zz on 4/8/17.
 */

public class QRClassifier {
    private static final String[] attributes = {" ", "https?://", "www\\.", "(\\.com)|(\\.org)|(\\.gov)|(\\.net)|(\\.edu)",
            "MATMSG", "(SUB:.*;)|(subject)", "BODY:.*;", "mailto:",
            "N:.*;", "ADR:.*;", "TEL:.*;", "EMAIL:.*;"};

    private DecisionTreeNode decisionTree;

    public QRClassifier() {

    }

    public void setTree(DecisionTreeNode tree) {
        this.decisionTree = tree;
    }

    public void train() throws IOException {

        boolean[][] features = new boolean[25][attributes.length];
        String[] outputs = new String[25];
        CSVReader reader = new CSVReader(new FileReader("/home/zz/Documents/cse4471/app/src/main/java/com/example/denny/qrcode/training.csv"));
        String [] nextLine;

        int index = 0;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            String actual = nextLine[0];
            String output = nextLine[1];
            for(int i = 0; i < attributes.length; i++) {
                Pattern pattern = Pattern.compile(attributes[i], Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(actual);
                features[index][i] = matcher.find();
            }
            outputs[index] = output;
            index++;
        }

        this.decisionTree = DecisionTreeNode.learn(features, outputs, attributes);
    }

    public String classify(String input) {
        return this.decisionTree.decide(input);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        QRClassifier qr = new QRClassifier();
        qr.train();

        System.out.println(qr.classify("https://google.com"));
        System.out.println(qr.classify("http://weixin.qq.com/r/56EHH2DE-z2ArSR89-T1"));
        System.out.println(qr.classify("The Hypertext Transfer Protocol (HTTP) is an application protocol for distributed,"));
        System.out.println(qr.classify("Mailto:871239928@qq.com;SUB:http;Body: hey\nhttp represents hypertext transfer protocol"));

        qr.decisionTree.dump();

    }
}
