package com.paymo.digitalvallet.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Scanner;

import com.paymo.digitalvallet.User;
import com.paymo.digitalvallet.UserGraph;
import com.paymo.digitalvallet.UserTransaction;

/**
 * Processes the UserTransactions. Reads the batch payments and creates the
 * UserGraph out of it. Then processes the stream of payments/transactions. If a
 * transaction is trusted according to feature 1 "trusted" is appended to output
 * file. Same for feature 2 and 3. For more details on features read Readme :-)
 * 
 * @author Mrudula
 *
 */
public class UserTransactionProcessor {

	private static String batchPaymentFile = null; // "./paymo_input/batch_payment.csv";

	private static String streamPaymentFile = null; // "./paymo_input/stream_payment.csv";

	private static String feature1File = null; // "./paymo_output/output1.txt";

	private static String feature2File = null;// "./paymo_output/output2.txt";

	private static String feature3File = null;// "./paymo_output/output3.txt";

	private static UserGraph graph;

	public static void main(String[] s) {
		try {

			if (s.length != 5)
				System.out
						.println("Invalid number of arguments. Please specify, the full name of two input and three output files.");

			init(s); // initialize the graph

			createUserGraph(); // process batch_payment and create user
								// transaction graph
			// graph.printGraph();
			readStreamPayments(); // now process the stream_payments
		} catch (FileNotFoundException e) {
			System.out.println("File batch_payment.csv not found!");
		} catch (ParseException e) {
			System.out.println("Invalid Date in input!");
			e.printStackTrace();

		} catch (NumberFormatException e) {
			System.out.println("Invalid number data in input!");
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void init(String[] s) {
		graph = new UserGraphImplWithMap();
		// TODO check if files exist
		batchPaymentFile = s[0];
		streamPaymentFile = s[1];
		feature1File = s[2];
		feature2File = s[3];
		feature3File = s[4];
	}

	/**
	 * Reads the batch payments/user transactions and build a data structure
	 * (graph like)
	 * 
	 * @throws IOException
	 */
	private static void createUserGraph() throws IOException,
			NumberFormatException, ParseException {
		// TODO parse date also. Currently date parsing is ignored as we do not
		// need that in determining feature 1, 2 or 3.
		System.out.println("Processing batch transactions...");
		FileInputStream fis = null;
		Scanner sc = null;

		try {
			fis = new FileInputStream(new File(batchPaymentFile));
			sc = new Scanner(fis, "UTF-8");
			String line = sc.nextLine();

			long startTimeAll = System.currentTimeMillis();
			int i = 0;
			while (sc.hasNextLine()) {

				line = sc.nextLine();

				String[] values = line.split(",");
				if (values.length == 5) {
					// SimpleDateFormat sdf = new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2016-11-02
					// 09:38:53
					// Date dateOfTransaction = sdf.parse(values[0]);
					Integer id1 = Integer.valueOf(values[1].trim());
					Integer id2 = Integer.valueOf(values[2].trim());
					Float amount = Float.valueOf(values[3].trim());
					User u1 = new User(id1);
					User u2 = new User(id2);
					UserTransaction t = new UserTransaction(null, u1, u2,
							amount);
					graph.addTransaction(t);

				}
				i++;
			}
			long endTimeAll = System.currentTimeMillis();
			System.out.println(" Number of transactions processed = "
					+ String.valueOf(i));
			
			System.out.println(" Time taken = "
					+ String.valueOf(endTimeAll - startTimeAll) + " ms");
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			fis.close();
			sc.close();
		}
	}

	/**
	 * Reads the stream of payments/user transactions and appends whether the
	 * transaction is trusted/unverified in the output file(s)
	 * 
	 * @throws IOException
	 */
	private static void readStreamPayments() throws IOException {
		System.out.println("Processing transaction stream...");
		FileInputStream fis = null;
		Scanner sc = null;
		FileOutputStream fos1 = null, fos2 = null, fos3 = null;
		Writer writer1 = null, writer2 = null, writer3 = null;

		try {
			fis = new FileInputStream(new File(streamPaymentFile));
			sc = new Scanner(fis, "UTF-8");
			String line = sc.nextLine();
			long timeToAdd = 0;

			fos1 = new FileOutputStream(new File(feature1File), false);
			fos2 = new FileOutputStream(new File(feature2File), false);
			fos3 = new FileOutputStream(new File(feature3File), false);

			writer1 = Channels.newWriter(fos1.getChannel(), "UTF-8");
			writer2 = Channels.newWriter(fos2.getChannel(), "UTF-8");
			writer3 = Channels.newWriter(fos3.getChannel(), "UTF-8");

			long startTimeAll = System.currentTimeMillis();
			long i = 0;
			while (sc.hasNextLine()) {
				i++;
				line = sc.nextLine();
				String[] values = line.split(",");
				if (values.length == 5) {
					// SimpleDateFormat sdf = new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2016-11-02
					// 09:38:53
					// Date dateOfTransaction = sdf.parse(values[0]); Really
					// slowing down, so commented
					Integer id1 = Integer.valueOf(values[1].trim());
					Integer id2 = Integer.valueOf(values[2].trim());
					Float amount = Float.valueOf(values[3].trim());
					User u1 = new User(id1);
					User u2 = new User(id2);
					UserTransaction t = new UserTransaction(null, u1, u2,
							amount);

					boolean isFirstTransaction = graph
							.isFirstTransactionForUsers(t);
					if (isFirstTransaction) {
						writer1.append("unverified\n");
						if (graph.isCommonFriendsTransaction(t)) {
							writer2.append("trusted\n");
							writer3.append("trusted\n");
						} else {
							writer2.append("unverified\n");
							if (graph.is4thDegreeFriendsTransaction(t)) {
								writer3.append("trusted\n");
							} else {
								writer3.append("unverified\n");
							}
						}
					} else {
						writer1.append("trusted\n");
						writer2.append("trusted\n");
						writer3.append("trusted\n");
					}

					if (i % 9000 == 0) {
						writer1.flush();
						writer2.flush();
						writer3.flush();
					}

					if (i % 100000 == 0) {
						System.out.println(" Currently at transaction = " + i);
					}
				}

			}
			long endTimeAll = System.currentTimeMillis();
			System.out.println(" Number of streams processed = " + i);
			long timeTaken = endTimeAll - startTimeAll;
			
			System.out.println(" Total Time taken = "
					+ String.valueOf(timeTaken) + " ms");
			float average = timeTaken/i;
			DecimalFormat df = new DecimalFormat("###.##");
			
			System.out.println(" Average Time taken per line = "
					+ String.valueOf(df.format(average)) + " ms");
			
		} catch (IOException e) {
			throw e;

		} finally {
			fis.close();
			sc.close();
			writer1.flush();
			writer2.flush();
			writer3.flush();
			fos1.close();
			fos2.close();
			fos3.close();

		}

	}

}
