package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
{
    final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

    public void renderTileEntityAt(TileEntityPiston te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        BlockPos pos = te.getPos();
        IBlockState state = te.getPistonState();
        Block b = state.getBlock();

        if (b.getMaterial() != Material.air && te.getProgress(partialTicks) < 1.0F)
        {
            Tessellator t = Tessellator.getInstance();
            WorldRenderer wr = t.getWorldRenderer();
            bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.shadeModel(Minecraft.getMinecraft().gameSettings.ambientOcclusion != 0 ? 7425 : 7424);

            wr.begin(7, DefaultVertexFormats.BLOCK);
            wr.setTranslation((double)((float)x - (float)pos.getX() + te.getOffsetX(partialTicks)), (double)((float)y - (float)pos.getY() + te.getOffsetY(partialTicks)), (double)((float)z - (float)pos.getZ() + te.getOffsetZ(partialTicks)));
            World world = this.getWorld();

            if (b == Blocks.piston_head && te.getProgress(partialTicks) < 0.5F)
            {
                state = state.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(state, world, pos), state, pos, wr, true);
            }
            else if (te.shouldPistonHeadBeRendered() && !te.isExtending())
            {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = b == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate1 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty(BlockPistonExtension.FACING, state.getValue(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(te.getProgress(partialTicks) >= 0.5F));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate1, world, pos), iblockstate1, pos, wr, true);
                wr.setTranslation((double)((float)x - (float)pos.getX()), (double)((float)y - (float)pos.getY()), (double)((float)z - (float)pos.getZ()));
                state.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(state, world, pos), state, pos, wr, true);
            }
            else
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(state, world, pos), state, pos, wr, false);

            wr.setTranslation(0.0D, 0.0D, 0.0D);
            t.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }
}
