package net.bergbauer.better_pvp.mixin;

import net.bergbauer.better_pvp.PlayerColorLoader;
import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"), cancellable = true)
    public void modifyChatMessage(net.minecraft.text.Text message, net.minecraft.network.message.MessageSignatureData signatureData, net.minecraft.client.gui.hud.MessageIndicator indicator, CallbackInfo ci) {
        if (message == null || !Settings_Screen.isSettingEnabled("Teams activated") || !Settings_Screen.isSettingEnabled("Show teams in chat")) return;

        // Spielerfarben laden
        PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
        Map<String, TextColor> userColors = PlayerColorLoader.USER_COLORS;

        // Ziel: Erhalte die ursprüngliche Nachricht, ändere aber nur Spielernamen
        MutableText resultMessage = Text.literal(""); // Die resultierende Nachricht wird hier aufgebaut

        // Die ursprüngliche Nachricht durchlaufen, um Farben und Textteile zu bewahren
        message.visit((style, text) -> {
            int index = 0;

            // Solange es noch Text zu verarbeiten gibt
            while (index < text.length()) {
                boolean matched = false;

                // Spielername suchen
                for (Map.Entry<String, TextColor> entry : userColors.entrySet()) {
                    String username = entry.getKey();
                    TextColor color = entry.getValue();

                    // Überprüfen, ob an der aktuellen Position ein Spielername steht
                    if (text.startsWith(username, index)) {
                        // Text vor dem Spielernamen hinzufügen (falls vorhanden)


                        // Spielername in der zugewiesenen Farbe hinzufügen
                        resultMessage.append(Text.literal(username).setStyle(style.withColor(color)));

                        // Überspringe den Spielernamen
                        index += username.length();
                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    // Kein Spielername: Füge das nächste Zeichen hinzu
                    resultMessage.append(Text.literal(String.valueOf(text.charAt(index))).setStyle(style));
                    index++;
                }
            }

            return Optional.empty(); // Fortfahren
        }, Style.EMPTY);

        // Ersetze die ursprüngliche Nachricht durch die bearbeitete
        ChatHudLine chatHudLine = new ChatHudLine(MinecraftClient.getInstance().inGameHud.getTicks(), resultMessage, signatureData, indicator);
        this.logChatMessage(chatHudLine);
        this.addVisibleMessage(chatHudLine);
        this.addMessage(chatHudLine);

        ci.cancel(); // Originalnachricht nicht mehr anzeigen
    }

    @Shadow
    protected abstract void logChatMessage(ChatHudLine message);

    @Shadow
    protected abstract void addVisibleMessage(ChatHudLine message);

    @Shadow
    protected abstract void addMessage(ChatHudLine message);
}
