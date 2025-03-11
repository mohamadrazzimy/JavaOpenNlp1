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
