package net.bergbauer.better_pvp.mixin;

import net.bergbauer.better_pvp.PlayerColorLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"), cancellable = true)
    public void modifyChatMessage(net.minecraft.text.Text message, net.minecraft.network.message.MessageSignatureData signatureData, net.minecraft.client.gui.hud.MessageIndicator indicator, CallbackInfo ci) {
        if (message == null) return;
        PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
        // Originalnachricht als bearbeitbaren Text kopieren
        MutableText newMessage = message.copy();
        // Nachricht als String extrahieren
        String originalMessage = newMessage.getString();
        // Überprüfen, ob ein definierter Benutzername in der Nachricht vorkommt
        for (String username : PlayerColorLoader.USER_COLORS.keySet()) {
            int startIndex = originalMessage.indexOf(username);
            if (startIndex == -1) continue; // Benutzername nicht gefunden, überspringen

            TextColor color = PlayerColorLoader.USER_COLORS.get(username);

            // Indexe für vor und nach dem Benutzernamen berechnen
            int endIndex = startIndex + username.length();

            // Teile die Nachricht in vor, Benutzername und nach Benutzername auf
            MutableText before = Text.literal(originalMessage.substring(0, startIndex));
            MutableText coloredName = Text.literal(username).setStyle(Style.EMPTY.withColor(color));
            MutableText after = Text.literal(originalMessage.substring(endIndex));

            // Baue die neue Nachricht zusammen
            newMessage = before.append(coloredName).append(after);
            break; // Kein weiteres Matching nötig
        }

        // Überschreibe die ursprüngliche Nachricht
        ChatHudLine chatHudLine = new ChatHudLine(MinecraftClient.getInstance().inGameHud.getTicks(), newMessage, signatureData, indicator);
        //((ChatHud) (Object) this).addMessage(newMessage, signatureData, indicator);
        this.logChatMessage(chatHudLine);
        this.addVisibleMessage(chatHudLine);
        this.addMessage(chatHudLine);
        ci.cancel(); // Verhindert, dass die ursprüngliche Nachricht gerendert wird
    }

    @Shadow
    protected abstract void logChatMessage(ChatHudLine message);

    @Shadow
    protected abstract void addVisibleMessage(ChatHudLine message);

    @Shadow
    protected abstract void addMessage(ChatHudLine message);

}