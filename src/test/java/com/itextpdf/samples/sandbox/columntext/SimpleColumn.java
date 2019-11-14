/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34445641
 */
package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SimpleColumn {
    public static final String DEST
            = "./target/sandbox/columntext/simple_column.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleColumn().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(100, 120));

        Paragraph paragraph = new Paragraph("REALLLLLLLLLLY LONGGGGGGGGGG text").setFontSize(4.5f);
        paragraph.setNextRenderer(new ParagraphRenderer(paragraph) {
            @Override
            public List<Rectangle> initElementAreas(LayoutArea area) {
                List<Rectangle> list = new ArrayList<Rectangle>();
                list.add(new Rectangle(9, 70, 61, 25));
                return list;
            }

            // If renderer overflows on the next area itext will use default getNextRender() method with default renderer
            // parameters. So the method should be overrided with the parameters from the initial renderer
            @Override
            public IRenderer getNextRenderer() {
                return new ParagraphRenderer((Paragraph) modelElement);
            }
        });
        doc.add(paragraph);

        doc.close();
    }
}
