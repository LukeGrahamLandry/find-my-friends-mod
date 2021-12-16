package ca.lukegrahamlandry.findmyfriends.client.gui;

import ca.lukegrahamlandry.findmyfriends.ModMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Drawable {
    private IDrawable d;
    ResourceLocation texture;
    int w;
    int h;

    public Drawable(String texturePath, int width, int height){
        this(new ResourceLocation(ModMain.MOD_ID, texturePath), width, height);
    }

    public Drawable(ResourceLocation textureIn, int width, int height){
        this.texture = textureIn;
        this.w = width;
        this.h = height;
        this.d = new Drawable.UITexture(texture, w, h).getArea(0, 0, w, h);
    }

    public void draw(int x, int y){
        d.draw(x, y, w, h);
    }

    public interface IDrawable {
        default void draw(double x, double y, double width, double height) {
            drawPartial(x, y, width, height, 0, 0, 1, 1);
        }

        void drawPartial(double x, double y, double width, double height, float x1, float y1, float x2, float y2);
    }

    public static class UITexture {
        private final ResourceLocation TEXTURE;
        private final float x;
        private final float y;

        public UITexture(ResourceLocation texture, float x, float y) {
            TEXTURE = texture;
            this.x = x;
            this.y = y;
        }

        public void bindTexture() {
            Minecraft.getInstance().textureManager.bind(TEXTURE);
        }

        public IDrawable getFullArea() {
            return new Area(0, 0, 1, 1);
        }

        public IDrawable getArea(int x, int y, int w, int h) {
            return new Area(x / w, y / h, w / this.x, h / this.y);

        }

        private class Area implements IDrawable {

            private final float v, u;
            private final float dv, du;

            public Area(float u, float v, float du, float dv) {
                this.v = v;
                this.u = u;
                this.du = du;
                this.dv = dv;
            }

            @Override
            public void drawPartial(double x, double y, double width, double height, float x1, float y1, float x2,
                                    float y2) {
                bindTexture();
                double xi = x + width * x1;
                double xf = x + width * x2;
                double yi = y + height * y1;
                double yf = y + height * y2;
                float ui = u + du * x1;
                float uf = u + du * x2;
                float vi = v + dv * y1;
                float vf = v + dv * y2;

                Tessellator tesselator = Tessellator.getInstance();
                BufferBuilder buffbuilder = tesselator.getBuilder();
                buffbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buffbuilder.vertex(xi, yi, 0D).uv(ui, vi).endVertex();
                buffbuilder.vertex(xi, yf, 0D).uv(ui, vf).endVertex();
                buffbuilder.vertex(xf, yf, 0D).uv(uf, vf).endVertex();
                buffbuilder.vertex(xf, yi, 0D).uv(uf, vi).endVertex();
                tesselator.end();

            }

        }
    }
}
