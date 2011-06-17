package org.mskcc.portal.util;

import org.mskcc.portal.model.GeneCounterPrefs;

import java.util.HashMap;

//TODO: I think this can be deleted, as it is replaced by OncoSpec language parser

/**
 * parse a list of genes with 'microencodings' as submitted in the Portal Web form.
 * A microencoding has the form:
 * geneSymbol:CE
 * where :CE are optional and C and E, denoting copy number and Expression, respectively, are members of { -, +, ! }, meaning, 
 * respectively, set down, set up, and 'ignore'.
 * 
 * @author Ethan Cerami
 */
public class GenePrefsParser {

    public static HashMap <String, GeneCounterPrefs> getGenePrefs (String userGeneListStr) {
        HashMap <String, GeneCounterPrefs> geneCounterPrefMap = new HashMap<String, GeneCounterPrefs>();

        String genes[] = userGeneListStr.trim().split("\\s+");
        for (String gene : genes) {
           geneCounterPrefMap.put( parseGeneSymbol(gene), parseGeneWithMicroEncoding( gene ) );
        }
        return geneCounterPrefMap;
    }
    
    public static String parseGeneSymbol( String geneWithMicroEncoding ){
       
       validateGeneWithMicroEncoding( geneWithMicroEncoding );
       if (geneWithMicroEncoding.contains(":")) {
          String parts[] = geneWithMicroEncoding.split(":");
          return parts[0];
       } else {
          return geneWithMicroEncoding;
       }
    }
       
    private static void validateGeneWithMicroEncoding( String geneWithMicroEncoding ){

       if (geneWithMicroEncoding.contains(":")) {
          String parts[] = geneWithMicroEncoding.split(":");
          if (parts.length != 2) {
              throw new IllegalArgumentException ("Illegal String:  " + geneWithMicroEncoding);   
          }

          String prefsString = parts[1];
          if (prefsString.length() != 2) {
              throw new IllegalArgumentException ("Illegal String:  " + geneWithMicroEncoding);
          }
       }
    }
       
    public static GeneCounterPrefs parseGeneWithMicroEncoding( String gene ){
       GeneCounterPrefs prefs = new GeneCounterPrefs();
       prefs.setIgnoreCna(false);
       prefs.setIgnoreMrna(false);

       validateGeneWithMicroEncoding( gene );

       if (gene.contains(":")) {
           String parts[] = gene.split(":");

           String prefsString = parts[1];

           char cnaPref = prefsString.charAt(0);
           if (cnaPref == '-') {
               prefs.setCnaDown(true);
           }
           if (cnaPref == '+') {
               prefs.setCnaUp(true);
           }
           if (cnaPref == '!') {
               prefs.setIgnoreCna(true);
           }
           char mrnaPref = prefsString.charAt(1);
           if (mrnaPref == '-') {
               prefs.setMrnaDown(true);
           }
           if (mrnaPref == '+') {
               prefs.setMrnaUp(true);
           }
           if (mrnaPref == '!') {
               prefs.setIgnoreMrna(true);
           }

       }
       return prefs;
    }

}
