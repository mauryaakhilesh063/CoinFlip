package com.kfs.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Service
public class kfsService {


	private static final int MONTHS_IN_A_YEAR = 12;
	private static final int number = 1;


	public void KFSPdf(float interestRates,double balances,int term)
	{
		final String x2= "1";
		int x3 = Integer.parseInt(x2)  + 1;
		System.out.println(x3);

		int tenure = 12;
		long Interest = 15;
		float interestRate = Interest/MONTHS_IN_A_YEAR;
		int no_of_payments = tenure * MONTHS_IN_A_YEAR;
		double monthly_payment =Math.round((no_of_payments)*( 
				interestRate * (Math.pow(1+interestRate, no_of_payments)))/ (Math.pow(1+interestRate, no_of_payments))-1)*100/100;

		Document document = new Document();
		try {
			OutputStream outputStream1 = 
					new FileOutputStream(new File("C:\\Users\\akhilesh.maurya\\Desktop\\kfs.pdf"));
			PdfWriter writer =  PdfWriter.getInstance(document, outputStream1);
			document.open();
			Font nFont = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
			Paragraph paragraph = new Paragraph("Detailed Repayment Schedule", nFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			PdfPTable pTable = new PdfPTable(5);
			pTable.setWidthPercentage(100);
			pTable.setSpacingBefore(10f);
			pTable.setSpacingAfter(10f);
			float[] columnWidth = {10f,10f,10f,10f,10f};
			pTable.setWidths(columnWidth);
			PdfPCell pCell = new PdfPCell(new Paragraph("Instalment No.", nFont));
			PdfPCell pCell2 = new PdfPCell(new Paragraph("Outstanding Principal(in Rupees", nFont));
			PdfPCell pCell3 = new PdfPCell(new Paragraph("Principal(in Rupees", nFont));
			PdfPCell pCell4 = new PdfPCell(new Paragraph("Interest(in Rupess", nFont));
			PdfPCell pCell5 = new PdfPCell(new Paragraph("Installment(in Rupess" , nFont));
			pTable.addCell(pCell);
			pTable.addCell(pCell2);
			pTable.addCell(pCell3);
			pTable.addCell(pCell4);
			pTable.addCell(pCell5);
			//	for(int i=1;i<=tenure;i++) {

			interestRate=interestRates;
			double balance=  balances;
			int terms=term;
			System.out.println("----------------------");
			double monthlyRate =(interestRate)/1200;
			double payment  = 	balance * (monthlyRate/(1-Math.pow(1+monthlyRate, -terms)));
			for (int count = 0; count < terms; ++count)
			{
				double interest = 0;
				double monthlyPrincipal = 0;
				String count1= Long.toString(count+1); 
				//    	
				String balance1= Double.toString(balance); 
				pTable.addCell(new PdfPCell(new Paragraph(count1)));
				pTable.addCell(new PdfPCell(new Paragraph(balance1)));
				interest = (double) Math.round((balance * monthlyRate)*100/100);
				String interest1= Double.toString(interest); 
				pTable.addCell(new PdfPCell(new Paragraph(interest1)));

				monthlyPrincipal = Math.round((payment - (interest))*100/100);
				String monthlyPrincipal1= Double.toString(monthlyPrincipal);
				System.out.println("-----------------");
				pTable.addCell(new PdfPCell(new Paragraph(monthlyPrincipal1)));
				balance =Math.round( (balance - monthlyPrincipal)*100/100);	
				System.out.println("Interest is: " +interest);	
				double installment = (interest+monthlyPrincipal);
				double installments = Math.round((installment*100))/100;	
				String installment1 = Double.toString(installments);
				System.out.println("Installment is: " +installment1);
				pTable.addCell(new PdfPCell(new Paragraph (installment1)));
			}
			document.add(paragraph);
			document.add(pTable);

			document.close();
			writer.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
