package com.oup.eac.common.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Generate a script for DLP products
 *
 */
public class DLPProductScript {
    private static final String commentLine = "\n-- %s - %s - %s\n";
    private static final String productLine = "insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','%s', 0, 999999, 'http://dummypage', '%s', (select id from division where division_type='ELT'), null, null, null);\n";
    private static final String registrationLine = "insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '%s', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');\n";
    private static final String isbnLine = "--insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', '%s', 'PRODUCT', null, '%s');\n";

	private final String inputFilename;
	private final String outputFilename;

	public DLPProductScript(String inputFilename, String outputFilename) {
		this.inputFilename = inputFilename;
		this.outputFilename = outputFilename;
    }

    private void create() throws IOException {
		File outputFile = new File(outputFilename);
        outputFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));
		BufferedReader reader = new BufferedReader(new FileReader(inputFilename));

		int productsWritten = 0;
		String line;
		while ((line = reader.readLine()) != null) {
            String id = UUID.randomUUID().toString();
			String productName = getName(line);
			String isbn = getIsbn(line);
                        
			writeCommentLine(writer, productName, isbn, id);
			writeProductLine(writer, id, productName);
			writeRegistrationLine(writer, id);
			writeIsbnLine(writer, id, isbn);
			productsWritten++;
        }
        
		writer.flush();
		writer.close();
        
		System.out.println(String.format("Created script for %d products at %s", productsWritten, outputFile));
    }

    private String getName(String product) {
        String[] splitString = product.split(","); 
        return splitString[0]; 
    }

    private String getIsbn(String product) {
        String[] splitString = product.split(","); 
        return splitString.length == 2 ? splitString[1] : null; 
    }

    private void writeCommentLine(BufferedWriter bw, String productName, String isbn, String id) throws IOException {
        bw.write(String.format(commentLine, productName, isbn, id));
    }
    
    private void writeProductLine(BufferedWriter bw, String id, String productName) throws IOException {
        bw.write(String.format(productLine, id, productName));
    }


    private void writeRegistrationLine(BufferedWriter bw, String id) throws IOException {
        bw.write(String.format(registrationLine, id));        
    }

    private void writeIsbnLine(BufferedWriter bw, String id, String isbn) throws IOException {
        if (isbn != null) {
            bw.write(String.format(isbnLine, isbn, id));
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("The first argument must be the path to the input file, the second argument the path to the output file.");
			System.out.println("The input file should contain comma-separated titles and isbns, one per line.");
			System.exit(1);
		}
		DLPProductScript script = new DLPProductScript(args[0], args[1]);
        try {
            script.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
