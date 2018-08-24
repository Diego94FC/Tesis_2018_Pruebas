import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXLattice;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.criteria.HierarchicalDistanceTCloseness;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelSampleSummary;


public class Pruebas  {
    
   
    public static void main(String[] args) throws IOException {
        
        
        
        // Cargar bdd de entrada
        Data data = Data.create("data/adult.csv", StandardCharsets.UTF_8, ';');
        // Definir atributos, jerarquias y archivos de entrada de las mismas.
        data.getDefinition().setAttributeType("sex", Hierarchy.create("data/adult_hierarchy_sex.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("age", Hierarchy.create("data/adult_hierarchy_age.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("race", Hierarchy.create("data/adult_hierarchy_race.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("marital-status", Hierarchy.create("data/adult_hierarchy_marital-status.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("education", Hierarchy.create("data/adult_hierarchy_education.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("native-country", Hierarchy.create("data/adult_hierarchy_native-country.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("workclass", Hierarchy.create("data/adult_hierarchy_workclass.csv", StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("salary-class", Hierarchy.create("data/adult_hierarchy_salary-class.csv", StandardCharsets.UTF_8, ';'));     
        
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        
        config.setSuppressionLimit(0.99d);
        
        System.out.println("Elija el numero de la opcion de la tecnica a utilizar :");
        System.out.println("Opciones dispibles: 1, 2 y 3 :");
        System.out.println("1: K-anonimato");
        System.out.println("2: (c,L)-diversidad recursiva");
        System.out.println("3: T-cercania");
        Scanner scanner = new Scanner(System.in);
        String opcion = scanner.nextLine();
        
        switch (opcion)
        {
            case "1": System.out.println("Se selecciono K-anonimato");
                      //System.out.println("Ingrese el valor de K"); 
                      
                      String k ="0";
                      while(Integer.parseInt(k) < 2 || Integer.parseInt(k) > 25)
                      {
                      System.out.println("Ingrese el valor de K  (k debe ser mayor a 1 y menor a 26)"); 
                      k = scanner.nextLine();
                      }
                      
                      data.getDefinition().setAttributeType("occupation", AttributeType.INSENSITIVE_ATTRIBUTE);
                      config.addPrivacyModel(new KAnonymity(Integer.parseInt(k)));                      
                      break;
                      
            case "2": System.out.println("Se selecciono (c,L)-diversidad recursiva");
                      String kl ="0";
                      while(Integer.parseInt(kl) < 2 || Integer.parseInt(kl) > 25)
                      {
                      System.out.println("Ingrese el valor de K  (k debe ser mayor a 1 y menor a 26)"); 
                      kl = scanner.nextLine();
                      }
                      
                      String c ="0";
                      while(Integer.parseInt(c) < 2 || Integer.parseInt(c) > 20)
                      {
                      System.out.println("Ingrese el valor de c  (c debe ser mayor a 1 y menor a 20)"); 
                      c = scanner.nextLine();
                      }                      
                      
                      String l ="0";
                      while(Integer.parseInt(l) < 2 || Integer.parseInt(l) > 15)
                      {
                      System.out.println("Ingrese el valor de L  (L debe ser mayor a 1 y menor a 16)"); 
                      l = scanner.nextLine();
                      }
                      
                      data.getDefinition().setAttributeType("occupation", AttributeType.SENSITIVE_ATTRIBUTE);
                      config.addPrivacyModel(new KAnonymity(Integer.parseInt(kl)));
                      config.addPrivacyModel(new RecursiveCLDiversity("occupation", Integer.parseInt(c), Integer.parseInt(l)));
                      break;
                      
            case "3": System.out.println("Se selecciono T-cercania");
                      
                      String kt ="0";
                      while(Integer.parseInt(kt) < 2 || Integer.parseInt(kt) > 25)
                      {
                      System.out.println("Ingrese el valor de K  (k debe ser mayor a 1 y menor a 26)"); 
                      kt = scanner.nextLine();
                      }
                      
                      String t ="-1";
                      while(Float.valueOf(t) <= 0 || Float.valueOf(t) > 1)
                      {
                      System.out.println("Ingrese el valor de T (entro 0 y 1)");
                      t = scanner.nextLine();
                      }
                      
                                            
                      data.getDefinition().setAttributeType("occupation", AttributeType.SENSITIVE_ATTRIBUTE);
                      config.addPrivacyModel(new KAnonymity(Integer.parseInt(kt)));
                      Hierarchy jerar = Hierarchy.create("data/adult_hierarchy_occupation.csv", StandardCharsets.UTF_8, ';');
                      config.addPrivacyModel(new HierarchicalDistanceTCloseness("occupation", Float.valueOf(t),jerar ));         
                      break;
                      
            default: System.out.println("Opcion invalida. Ejecucion finalizada");
                     System.exit(0);
                     break;
        }
        
        
        DataHandle inHandle = data.getHandle();
        System.out.println("Cargada la base de datos 'data/adult.csv'");
        System.out.println("Contiene: "+inHandle.getNumRows()+" registros");
        String visualizar ="";
        while(!visualizar.equals("si") && !visualizar.equals("no"))
        {
        System.out.println("¿Desea visualizar la tabla anonimizada? (si/no)"); 
        visualizar = scanner.nextLine();
        }
        
        //Datos de entrada, no serán mostrados
        //print(inHandle.getView());
        
        
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle outHandle = result.getOutput(false);
        
        //Calcula y muestra el riesgo en pantalla
        System.out.println("\n***********************************");
        System.out.println("     Comenzando anonimizacion");
        System.out.println("***********************************\n");
        
        //Indices de las columnas con las cuales ordenar el resultado (para obtener un orden similar a clases de equivalencia)
        int[] index = {0,1,2,3,4,5,6,7};               
        outHandle.sort(true,index);
        //Muestra estadísticas útiles de los datos.
        printResult(result, data);
        
        //Muestra columnas con la anonimización resultante
        
        if(visualizar.equals("si"))
        {
        System.out.println(" Tabla Resultante: ");
        Iterator<String[]> transformed = result.getOutput(false).iterator();
        while (transformed.hasNext()) {
            System.out.print("   ");
            System.out.println(Arrays.toString(transformed.next()));
        }
        }
        
        //Cantidad de tuplas suprimidas
        int supressedRows = result.getOutput(result.getGlobalOptimum(), false).getStatistics().getEquivalenceClassStatistics().getNumberOfOutlyingTuples();
        
        /* Mostrar información necesaria para el análisis y comparación*/
        System.out.println("\n***********************************");
        System.out.println("     Estadísticas para comparar");
        System.out.println("***********************************");
        System.out.println("Promedio de edad datos reales: "+getAvgInputAge(inHandle, result));
        System.out.println("Promedio de edad con promedio del intervalo: "+getAvgAnonAgeAvg(outHandle, supressedRows));    
        System.out.println("Promedio de edad con limite inferior del intervalo: "+getAvgAnonAgeInf(outHandle, supressedRows));    
        System.out.println("Max de edad entrada: "+getMaxAgeInput(inHandle));
        System.out.println("Max de edad anonimizado: "+getMaxAgeAnon(outHandle));
        System.out.println("Min de edad entrada: "+getMinAgeInput(inHandle));
        System.out.println("min de edad anonimizado: "+getMinAgeAnon(outHandle));
        int[] mode = new int[2];
        mode = getModeAgeInput(inHandle);
        System.out.println("cantidad moda entrada: "+mode[0]);
        System.out.println("valor moda entrada: "+mode[1]);
        String[] modeAnon = new String[2];     
        modeAnon = getModeAgeAnon(outHandle);
        System.out.println("cantidad moda anon: "+modeAnon[0]);
        System.out.println("valor moda anon: "+modeAnon[1]); 
        System.out.println();
        getRisk(outHandle);
        System.out.println();
        
    }
    
    
    
     private static String getPrecent(double value)
    {
        return (int)(Math.round(value * 100)) + "%";
    }
    
    private static void getRisk(DataHandle handle)
    {
        RiskEstimateBuilder builder = handle.getRiskEstimator();
        RiskModelSampleSummary risk = builder.getSampleBasedRiskSummary(0.1d);
        System.out.println("Riesgo promedio: "+getPrecent(risk.getProsecutorRisk().getSuccessRate()));     
        System.out.println("Riesgo promedio: "+risk.getProsecutorRisk().getSuccessRate());   
        System.out.println("Riesgo maximo: "+getPrecent(risk.getProsecutorRisk().getHighestRisk()));     
        System.out.println("Riesgo maximo: "+risk.getProsecutorRisk().getHighestRisk());     
        System.out.println("Tuplas en riesgo: "+getPrecent(risk.getProsecutorRisk().getRecordsAtRisk()));
        System.out.println("Tuplas en riesgo: "+risk.getProsecutorRisk().getRecordsAtRisk());
    }
    
    private static int getAvgInputAge(DataHandle handle, ARXResult result)
    {
        int sum = 0;    
        for (int i=0; i< handle.getNumRows(); i++){
               sum += Integer.parseInt(handle.getValue(i, 1));
        }
        
        return (sum/handle.getNumRows());
    }
    
    
     private static int getAvgAnonAgeInf(DataHandle handle, int supressedRows)
    {
        int sumAnon = 0; 
        
        String temp = "";
        String[] number;
        for (int i=0; i< handle.getNumRows(); i++){
            if(!handle.getValue(i, 1).equals("*"))
            {
               temp = handle.getValue(i, 1);
               number = temp.split("-");
               sumAnon += Integer.parseInt(number[0]);               
            }
        }
        return (sumAnon/(handle.getNumRows() - supressedRows));
    }
     
     
       private static int getAvgAnonAgeAvg(DataHandle handle, int supressedRows)
    {
        int sumAnon2 = 0; 
        String temp2 = "";
        String[] number2;
        int avgInterval = 0;
        for (int i=0; i< handle.getNumRows(); i++){
            if(!handle.getValue(i, 1).equals("*"))
            {
               temp2 = handle.getValue(i, 1);
               number2 = temp2.split("-");
               avgInterval = (Integer.parseInt(number2[0]) + Integer.parseInt(number2[1]))/2;
               sumAnon2 += avgInterval;               
            }
        }
        return (sumAnon2 /(handle.getNumRows() - supressedRows));
    }
       
    private static int getMaxAgeInput(DataHandle handle)
    {
        int max = 0;    
        int maxTmp = 0;
        for (int i=0; i< handle.getNumRows(); i++){
               maxTmp = Integer.parseInt(handle.getValue(i, 1));
               if(maxTmp > max)
                   max = maxTmp;
        }
         
        return max;
    }
      

    private static int getMaxAgeAnon(DataHandle handle)
    {
        //Calcular Max de edad en DATOS Anonimizados
        int maxAnon = 0;    
        int maxTmpAnon = 0;
        String[] number3;
        for (int i=0; i< handle.getNumRows(); i++){
             if(!handle.getValue(i, 1).equals("*"))
             {
               number3 = handle.getValue(i, 1).split("-");
               maxTmpAnon = Integer.parseInt(number3[1]);
               if(maxTmpAnon > maxAnon)
                   maxAnon = maxTmpAnon;
               
             }
        }
        return maxAnon;
    }
    
    private static int getMinAgeInput(DataHandle handle)
    {
          int min = 100;    
        int minTmp = 0;
        for (int i=0; i< handle.getNumRows(); i++){
               minTmp = Integer.parseInt(handle.getValue(i, 1));
               if(minTmp < min)
                   min = minTmp;
        }
        return min;
        
    }
    
    private static int getMinAgeAnon(DataHandle handle)
    {
        int minAnon = 100;    
        int minTmpAnon = 0;
        String[] number4;
        for (int i=0; i< handle.getNumRows(); i++)
        {
             if(!handle.getValue(i, 1).equals("*"))
             {
               number4 = handle.getValue(i, 1).split("-");
               minTmpAnon = Integer.parseInt(number4[0]);
               if(minTmpAnon < minAnon)
                   minAnon = minTmpAnon;
               
             }
        }
        return minAnon;
    }
    
    private static int[] getModeAgeInput(DataHandle handle)
    {
        int valorModa = 0;
        int cantidadModa = 0;

        for (int i = 0; i < handle.getNumRows(); ++i) 
        {
        int count = 0;
        for (int j = 0; j < handle.getNumRows(); ++j) 
        {
            if (Integer.parseInt(handle.getValue(j, 1)) == Integer.parseInt(handle.getValue(i, 1))) 
                 ++count;
        }
        if (count > cantidadModa) 
        {
            cantidadModa = count;
            valorModa = Integer.parseInt(handle.getValue(i, 1));
        }
        }
        int[] output = new int[2];
        output[0] = cantidadModa;    
        output[1] = valorModa;    
        
        return output;
        
    }
    
    private static String[] getModeAgeAnon(DataHandle handle)
    {
        String valorModaAnon = "";
        int cantidadModaAnon = 0;
        String[] output = new String[2];

        for (int i = 0; i < handle.getNumRows(); ++i) 
        {
        int countAnon = 0;
        for (int j = 0; j < handle.getNumRows(); ++j) 
        {
            if (handle.getValue(j, 1).equals(handle.getValue(i, 1))) 
                 ++countAnon;
        }
        if (countAnon > cantidadModaAnon) 
        {
            cantidadModaAnon = countAnon;
            valorModaAnon = handle.getValue(i, 1);
        }
        }
        output[0] = Integer.toString(cantidadModaAnon);
        output[1] = valorModaAnon;
        
        return output;
    }
    
    
    protected static void printResult(final ARXResult result, final Data data) {

        // Print time
        final DecimalFormat df1 = new DecimalFormat("#####0.00");
        final String sTotal = df1.format(result.getTime() / 1000d) + "s";
        System.out.println("Tiempo necesario para anonimizar: " + sTotal);

        // Extract
        final ARXLattice.ARXNode optimum = result.getGlobalOptimum();
        final List<String> qis = new ArrayList<String>(data.getDefinition().getQuasiIdentifyingAttributes());

        if (optimum == null) {
            System.out.println(" - No se encontro solucion");
            return;
        }

        // Initialize
        final StringBuffer[] identifiers = new StringBuffer[qis.size()];
        final StringBuffer[] generalizations = new StringBuffer[qis.size()];
        int lengthI = 0;
        int lengthG = 0;
        for (int i = 0; i < qis.size(); i++) {
            identifiers[i] = new StringBuffer();
            generalizations[i] = new StringBuffer();
            identifiers[i].append(qis.get(i));
            generalizations[i].append(optimum.getGeneralization(qis.get(i)));
            if (data.getDefinition().isHierarchyAvailable(qis.get(i)))
                generalizations[i].append("/").append(data.getDefinition().getHierarchy(qis.get(i))[0].length - 1);
            lengthI = Math.max(lengthI, identifiers[i].length());
            lengthG = Math.max(lengthG, generalizations[i].length());
        }

        // Padding
        for (int i = 0; i < qis.size(); i++) {
            while (identifiers[i].length() < lengthI) {
                identifiers[i].append(" ");
            }
            while (generalizations[i].length() < lengthG) {
                generalizations[i].insert(0, " ");
            }
        }

        // Print
                System.out.println("Perdida de informacion: " + result.getGlobalOptimum().getLowestScore() + " / " + result.getGlobalOptimum().getHighestScore());
        System.out.println("Generalizacion optima");
        for (int i = 0; i < qis.size(); i++) {
            System.out.println("   * " + identifiers[i] + ": " + generalizations[i]);
        }
        System.out.println("\n**************************************************");
        System.out.println("    Estadisticas de las clases de equivalencia");
        System.out.println("**************************************************");
        System.out.println(result.getOutput(result.getGlobalOptimum(), false).getStatistics().getEquivalenceClassStatistics());
    }
}
