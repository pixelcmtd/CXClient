package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiNewChat extends Gui
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final List<String> sentMessages = Lists.<String>newArrayList();
    private final List<ChatLine> chatLines = Lists.<ChatLine>newArrayList();
    private final List<ChatLine> someChatLines = Lists.<ChatLine>newArrayList();
    private int scrollPos;
    private boolean isScrolled;

    public GuiNewChat(Minecraft mcIn)
    {
        this.mc = mcIn;
    }

    public void drawChat(int p_146230_1_)
    {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = this.someChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (k > 0)
            {
                if (this.isChatOpen())
                {
                    flag = true;
                }

                float f1 = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                for (int i1 = 0; i1 + this.scrollPos < this.someChatLines.size() && i1 < i; ++i1)
                {
                    ChatLine chatline = (ChatLine)this.someChatLines.get(i1 + this.scrollPos);

                    if (chatline != null)
                    {
                        int j1 = p_146230_1_ - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag)
                        {
                            double d0 = j1 / 200;
                            d0 = 1 - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp(d0, 0, 1);
                            d0 = d0 * d0;
                            int l1 = (int)(255 * d0);

                            if (flag)
                            {
                                l1 = 255;
                            }

                            l1 = (int)((float)l1 * f);
                            ++j;

                            if (l1 > 3)
                            {
                                int i2 = 0;
                                int j2 = -i1 * 9;
                                drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                this.mc.fontRendererObj.drawStringWithShadow(s, (float)i2, (float)(j2 - 8), 16777215 + (l1 << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (flag)
                {
                    int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = this.scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3)
                    {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages()
    {
        this.someChatLines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent chatMessage)
    {
        printChatMessageWithOptionalDeletion(chatMessage, 0);
    }

    /**
     * prints the ChatComponent to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(IChatComponent chatMessage, int index)
    {
        setChatLine(chatMessage, index, mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + chatMessage.getUnformattedText());
    }

    private void setChatLine(IChatComponent chatMessage, int index, int updateCounter, boolean someFlag)
    {
    	if(someChatLines.size() != 0 && chatMessage.getUnformattedText() == someChatLines.get(0).getChatComponent().getUnformattedText()) {
    		someChatLines.get(0).getChatComponent().appendText(" [++]");
    		if(!someFlag && chatLines.get(0).getChatComponent().getUnformattedText() == chatMessage.getUnformattedText())
    			chatLines.get(0).getChatComponent().appendText(" [++]");
    		return;
    	}
    	
        if (index != 0)
            deleteChatLine(index);

        int i = MathHelper.floor((float)getChatWidth() / getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.trimChatLinesForRendering(chatMessage, i, mc.fontRendererObj, false, false);
        boolean flag = isChatOpen();

        for (IChatComponent ichatcomponent : list)
        {
            if (flag && this.scrollPos > 0)
            {
                isScrolled = true;
                scroll(1);
            }

            someChatLines.add(0, new ChatLine(updateCounter, ichatcomponent, index));
        }

        if (!someFlag)
            chatLines.add(0, new ChatLine(updateCounter, chatMessage, index));
    }

    public void refreshChat()
    {
        this.someChatLines.clear();
        this.resetScroll();

        for (int i = this.chatLines.size() - 1; i >= 0; --i)
        {
            ChatLine chatline = (ChatLine)this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages()
    {
        return sentMessages;
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     */
    public void addToSentMessages(String p_146239_1_)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_))
            this.sentMessages.add(p_146239_1_);
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed, among others)
     */
    public void resetScroll()
    {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     */
    public void scroll(int p_146229_1_)
    {
        this.scrollPos += p_146229_1_;
        int i = this.someChatLines.size();

        if (this.scrollPos > i - this.getLineCount())
        {
            this.scrollPos = i - this.getLineCount();
        }

        if (this.scrollPos <= 0)
        {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    /**
     * Gets the chat component under the mouse
     */
    public IChatComponent getChatComponent(int x, int y)
    {
        if (!this.isChatOpen())
            return null;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaleFactor();
            float f = this.getChatScale();
            int j = x / i - 3;
            int k = y / i - 27;
            j = MathHelper.floor((float)j / f);
            k = MathHelper.floor((float)k / f);

            if (j >= 0 && k >= 0)
            {
                int l = Math.min(this.getLineCount(), this.someChatLines.size());

                if (j <= MathHelper.floor((float)this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l)
                {
                    int m = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;

                    if (m >= 0 && m < this.someChatLines.size())
                    {
                        ChatLine chatline = (ChatLine)this.someChatLines.get(m);
                        int n = 0;

                        for (IChatComponent ichatcomponent : chatline.getChatComponent())
                            if (ichatcomponent instanceof ChatComponentText)
                            {
                                n += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.removeFormattingIfDisabled(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));

                                if (n > j)
                                    return ichatcomponent;
                            }
                    }

                    return null;
                }
                else
                    return null;
            }
            else
                return null;
    }

    /**
     * Returns true if the chat GUI is open
     */
    public boolean isChatOpen()
    {
        return mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     */
    public void deleteChatLine(int id)
    {
        Iterator<ChatLine> iterator = this.someChatLines.iterator();

        while (iterator.hasNext())
        {
            ChatLine chatline = (ChatLine)iterator.next();

            if (chatline.getChatLineID() == id)
            {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext())
        {
            ChatLine chatline1 = (ChatLine)iterator.next();

            if (chatline1.getChatLineID() == id)
            {
                iterator.remove();
                break;
            }
        }
    }

    public int getChatWidth()
    {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight()
    {
        return calculateChatboxHeight(this.isChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    /**
     * Returns the chatscale from mc.gameSettings.chatScale
     */
    public float getChatScale()
    {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float p_146233_0_)
    {
        int i = 320;
        int j = 40;
        return MathHelper.floor(p_146233_0_ * (float)(i - j) + (float)j);
    }

    public static int calculateChatboxHeight(float p_146243_0_)
    {
        int i = 180;
        int j = 20;
        return MathHelper.floor(p_146243_0_ * (float)(i - j) + (float)j);
    }

    public int getLineCount()
    {
        return this.getChatHeight() / 9;
    }
}
