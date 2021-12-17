package ca.lukegrahamlandry.findmyfriends.client.render;

import ca.lukegrahamlandry.findmyfriends.entity.NamePlateEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class NamePlateRender extends EntityRenderer<NamePlateEntity> {
    public NamePlateRender(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    public void render(NamePlateEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack matrix, IRenderTypeBuffer renderType, int ticks) {
        ITextComponent name = entity.getName();

        // boolean flag = !entity.isDiscrete();
        float f = entity.getBbHeight() + 0.5F;
        int i = "deadmau5".equals(name.getString()) ? -10 : 0;
        matrix.pushPose();
        matrix.translate(0.0D, (double)f, 0.0D);
        matrix.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrix.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrix.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int)(f1 * 255.0F) << 24;
        FontRenderer fontrenderer = this.getFont();
        float f2 = (float)(-fontrenderer.width(name) / 2);

        fontrenderer.drawInBatch(name, f2, (float)i, 553648127, false, matrix4f, renderType, true, j, ticks);
        fontrenderer.drawInBatch(name, f2, (float)i, -1, false, matrix4f, renderType, false, 0, ticks);

        name = new StringTextComponent(Math.round(entity.dist) + " blocks away");
        f2 = (float)(-fontrenderer.width(name) / 2);
        // fontrenderer.drawInBatch(name, f2, (float)i + 2, 553648127, false, matrix4f, renderType, true, j, ticks);
        matrix.scale(0.75F, 0.75F, 0.75F);
        fontrenderer.drawInBatch(name, f2, (float)i - 10, 0x03ecfc, false, matrix4f, renderType, false, 0, ticks);

        matrix.popPose();
    }

    @Override
    public boolean shouldRender(NamePlateEntity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(NamePlateEntity p_110775_1_) {
        return null;
    }
}
