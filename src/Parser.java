import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marius on 11/11/2016.
 */
public class Parser {
    private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

    private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

    private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);

    private Tree parse(String str) {
        List<CoreLabel> tokens = tokenize(str);
        Tree tree = parser.apply(tokens);
        return tree;
    }

    /**
     *
     * @param str String to be operated
     * @return List of parsed elements
     */
    private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =
                tokenizerFactory.getTokenizer(
                        new StringReader(str));
        return tokenizer.tokenize();
    }

    /**
     *
     * @param str String to verify
     * @param p A regex pattern
     * @return True - if string contains a punctuation sign , False otherwise
     */
    private boolean isPunctuation(String str, Pattern p) {
        Matcher m = p.matcher(str);

        return str.length() == 1 && m.find();

    }

    /**
     *
     * @param str String containing a sentence (or a part of text)
     * @return A list of words from the main string
     */
    LinkedList<String> getTokenizedWords(String str){

        List<Tree> mLeaves;
        Pattern p = Pattern.compile("[.,!?\\-]");
        LinkedList<String> tokenizedWords = new LinkedList<>();
        Parser parser = new Parser();

        Tree tree = parser.parse(str);
        mLeaves = tree.getLeaves();

        for (Tree leaf : mLeaves) {
            String value = leaf.label().value();
            if (!isPunctuation(value, p)) {
                tokenizedWords.add(value);
            }
        }

        return tokenizedWords;
    }
}
