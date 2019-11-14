/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This question was answered by Bruno Lowagie in answer to the following question on StackOverflow:
 * http://stackoverflow.com/questions/30053684/how-to-add-border-to-paragraph-in-itext-pdf-library-in-java
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import java.io.File;

public class BorderForParagraph2 {
    public static final String DEST = "./target/sandbox/events/border_for_paragraph2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BorderForParagraph2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Hello,"));
        doc.add(new Paragraph("In this doc, we'll add several paragraphs that will trigger page events. " +
                "As long as the event isn't activated, nothing special happens, " +
                "but let's make the event active and see what happens:"));
        Paragraph renderedParagraph = new Paragraph("This paragraph now has a border. " +
                "Isn't that fantastic? By changing the event, we can even provide a background color, " +
                "change the line width of the border and many other things. Now let's deactivate the event.");
        renderedParagraph.setNextRenderer(new BorderParagraphRenderer(renderedParagraph));
        doc.add(renderedParagraph);
        doc.add(new Paragraph("This paragraph no longer has a border."));
        doc.add(new Paragraph("Let's repeat:"));
        for (int i = 0; i < 10; i++) {
            renderedParagraph = new Paragraph("This paragraph now has a border. Isn't that fantastic? " +
                    "By changing the event, we can even provide a background color, " +
                    "change the line width of the border and many other things. Now let's deactivate the event.");
            renderedParagraph.setNextRenderer(new BorderParagraphRenderer(renderedParagraph));
            doc.add(renderedParagraph);
            doc.add(new Paragraph("This paragraph no longer has a border."));
        }
        doc.close();
    }


    private class BorderParagraphRenderer extends ParagraphRenderer {
        public BorderParagraphRenderer(Paragraph modelElement) {
            super(modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            super.drawBorder(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            drawContext.getCanvas().rectangle(rect.getLeft(), rect.getBottom(),
                    rect.getWidth(), rect.getHeight());
            drawContext.getCanvas().stroke();
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overriden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new BorderParagraphRenderer((Paragraph) modelElement);
        }
    }
}
