/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34177025
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class TiledBackgroundColor2 {
    public static final String DEST
            = "./target/sandbox/tables/tiled_background_color2.pdf";

    public static final String IMG
            = "./src/test/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TiledBackgroundColor2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.addCell("Behold a cell with an image pattern:");

        Cell cell = new Cell();
        ImageData img = ImageDataFactory.create(IMG);
        cell.setNextRenderer(new TiledImageBackgroundRenderer(cell, img));
        cell.setHeight(60);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }


    private static class TiledImageBackgroundRenderer extends CellRenderer {
        protected ImageData img;

        public TiledImageBackgroundRenderer(Cell modelElement, ImageData img) {
            super(modelElement);
            this.img = img;
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overriden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TiledImageBackgroundRenderer((Cell) modelElement, img);
        }

        @Override
        public void draw(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            Rectangle position = getOccupiedAreaBBox();

            Image image = new Image(img);
            image.scaleToFit(10000000, position.getHeight());

            float x = position.getLeft();
            float y = position.getBottom();

            while (x + image.getImageScaledWidth() < position.getRight()) {
                image.setFixedPosition(x, y);
                canvas.addImage(img, x, y, image.getImageScaledWidth(), false);
                x += image.getImageScaledWidth();
            }
        }
    }
}
