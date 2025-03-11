OpenNLP
Natural Language Processing (NLP) is at the forefront of modern technology, powering applications from chatbots to sentiment analysis tools. For developers eager to explore NLP with Java, Apache OpenNLP provides a robust, user-friendly toolkit to kick-start their journey. This tutorial will guide you through building your first NLP application using Apache OpenNLP, particularly on the Replit platform.

Why Choose Apache OpenNLP?
Apache OpenNLP is a machine learning-based toolkit designed to process natural language text. It offers essential NLP capabilities such as tokenization, sentence detection, part-of-speech (POS) tagging, and named entity recognition (NER). Its ease of use and flexibility make it ideal for both beginners and experienced developers.

[1] Setting Up Your Project
To get started, use Replit, a cloud-based IDE that simplifies development by providing pre-configured environments.

[1.1] Create a Java project
Create a Java project in Replit using the “Java” template, which streamlines dependency management for Java applications. Give a suitable name to the project e.g. JavaOpenNlp1

[1.2] Prep Maven auto build and run configuration
Add maven executable path in replit Run configuration file.

Refer the following short notes for detailed steps:

How to create Java project on Replit platform?
To create a Java project on Replit, follow these steps: [1] Visit the Replit website at https://replit.com/ and log in…
shortnotes.razzi.my

[2] Update POM File

Replace with the following codes:

<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- JUnit for testing -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>

        <!-- Apache OpenNLP -->
        <dependency>
            <groupId>org.apache.opennlp</groupId>
            <artifactId>opennlp-tools</artifactId>
            <version>2.0.0</version>
        </dependency>
    </dependencies>
</project>
Code explanation:
[2.1] Build properties:
maven.compiler.source: Specifies the Java version for source code compatibility (Java 11 in this case).
maven.compiler.target: Specifies the target JVM version for the compiled bytecode (Java 11).
These properties ensure your project uses Java 11 for both source code and compiled output.

[2.2] Apache OpenNLP Dependency:
<dependency>
    <groupId>org.apache.opennlp</groupId>
    <artifactId>opennlp-tools</artifactId>
    <version>2.0.0</version>
</dependency>
groupId: Identifies Apache OpenNLP as the source of the library.
artifactId: Refers to the specific OpenNLP module (opennlp-tools).
version: Specifies the version to use (2.0.0).
[3] Edit main file
[3.1] Simple Tokenizer
Replace the content of Main.Java file with the following codes:

import opennlp.tools.tokenize.SimpleTokenizer;

public class Main {
    public static void main(String[] args) {
        // Example text
        String sentence = "Apache OpenNLP is a machine learning-based toolkit for processing natural language text.";

        // Initialize SimpleTokenizer
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        // Tokenize the sentence
        String[] tokens = tokenizer.tokenize(sentence);

        // Print the tokens
        System.out.println("Tokens:");
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}
Click the Run button.


Output:


[3.2] Sentence Detection
Download English model into folder /model:

wget -P model https://dlcdn.apache.org/opennlp/models/ud-models-1.2/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin

Replace the content of Main.Java file with the following codes:

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        try {
            // Path to the model
            File modelFile = new File("model/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin");

            // Load the model
            InputStream modelStream = new FileInputStream(modelFile);
            SentenceModel model = new SentenceModel(modelStream);

            // Initialize SentenceDetectorME
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

            // Input text
            String paragraph = "Dr. Smith, who has a Ph.D. in AI, gave a lecture on NLP. He said, 'Machine learning models are transforming industries worldwide.' "
                + "However, challenges remain: data privacy, scalability, and model interpretability. For example, the U.S. has strict regulations. "
                + "But progress continues—companies like OpenAI, Google, and Microsoft innovate constantly.";


            // Detect sentences
            String[] sentences = sentenceDetector.sentDetect(paragraph);

            // Print sentences
            System.out.println("Detected Sentences:");
            for (String sentence : sentences) {
                System.out.println(sentence);
            }

            // Close the stream
            modelStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
Click the Run button.


Output:


[3.3] Text categorization
Create a sample of annotated text file in model/training-data.txt:

positive I love this product! It's amazing.
positive This is fantastic. I really like it.
negative This is the worst experience I've ever had.
negative I hate this product. It's terrible.
neutral The product is okay, nothing special.
neutral It's average. Not bad, not great.
Replace the content of Main.Java file with the following codes:

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Step 1: Train the model
            DoccatModel model = trainModel("model/training-data.txt");

            // Step 2: Use the model to categorize text
            categorizeText(model, "I love this product! It's fantastic.");
            categorizeText(model, "This is the worst experience I've ever had.");
            categorizeText(model, "It's okay, but nothing special.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Train the model using labeled training data
    public static DoccatModel trainModel(String trainingFilePath) throws IOException {
        InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File(trainingFilePath));
        ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 100);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);

        DoccatFactory factory = new DoccatFactory();
        DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);

        System.out.println("Model training completed!");
        return model;
    }

    // Categorize input text using the trained model
    public static void categorizeText(DoccatModel model, String text) {
        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);

        // Tokenize the input text
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);

        // Categorize the tokenized text
        double[] outcomes = categorizer.categorize(tokens);
        String category = categorizer.getBestCategory(outcomes);

        System.out.println("Input: \"" + text + "\"");
        System.out.println("Category: " + category);
        System.out.println();
    }
}
Click the Run button.


Output:


Conclusion
Through three projects — simple tokenization, sentence detection, and text categorization — we’ve learned how to use Apache OpenNLP for basic NLP tasks. The simple tokenizer showed how to break text into smaller parts, like words. Sentence detection helped identify complete sentences in a text, making it easier to process. The text categorizer demonstrated how to classify text into groups, like positive or negative sentiment. These projects offer a straightforward way to start building text-processing applications with OpenNLP.

🤓

https://blog.devgenius.io/building-your-first-nlp-application-in-java-a-beginners-guide-to-apache-opennlp-8a531d6827ff
