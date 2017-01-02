import org.apache.jena.query.*;
import java.util.ArrayList;

/**
 * Created by Marius on 11/12/2016.
 */
public class OntologyParser {

    private String m_str;

    OntologyParser(String str){
        this.m_str = str;
    }

    /**
     *
     * @param url Url string , queried from DBPedia
     * @return Last element from the url - E.g : <http:/abc/bnm> , returns bnm
     */
    private static String getOnlyLastWordFromStringUrl(String url){

        String[] splits = url.split("/");

        String[] lastWord = splits[splits.length-1].split(">");
        return lastWord[0];
    }
    
    /**
     *
     * @return Getting the ontology classes from the db (SPARQL)
     */
    ArrayList<String> getOntologies(){
        ArrayList<String> ontologies = new ArrayList<>();

        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
                "PREFIX : <http://dbpedia.org/resource/>\n" +
                "SELECT ?ontology WHERE { :"+this.m_str+ " a ?ontology filter( regex(?ontology, \"ontology\" ) ||" +
                " regex(?ontology, \"class\" )) }";
        org.apache.jena.query.Query query = null;

        try {
            query = QueryFactory.create(queryString);
        }

        catch (Exception e){
            System.out.println("Exception creating query, invalid input");
        }

        try (QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            ResultSet results = exec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                String sSol = sol.toString();

                if (!sSol.isEmpty()) {
                    ontologies.add(getOnlyLastWordFromStringUrl(sSol));
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong, invalid input");
        }

        return ontologies;
    }

}
