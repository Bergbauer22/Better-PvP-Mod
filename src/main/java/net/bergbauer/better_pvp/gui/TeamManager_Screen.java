package net.bergbauer.better_pvp.gui;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TeamManager_Screen extends Screen {
    // Variablen
    public ButtonWidget backButton;
    public ButtonWidget createTeamButton;
    private final List<TeamCategoryButton> teamObjects;
    private final int spacing = 30;
    private static final String CONFIG_PATH = "config/team_name.txt"; // Pfad zur Speicherkonfiguration

    // Konstruktor
    public TeamManager_Screen() {
        super(Text.literal("TeamManager_Screen"));
        this.teamObjects = new ArrayList<>(); // Initialisiere die Team-Liste
    }


    public void close() {
        saveTeamObjects(); // Speichert die Team-Objekte beim Schließen des Screens
        super.close();
    }
    @Override
    protected void init() {
        loadTeamObjects();
        backButton = ButtonWidget.builder(Text.literal("Back"), button -> {
                    saveTeamObjects();
                    MinecraftClient.getInstance().setScreen(new BetterPvP_MenuScreen());
                })
                .dimensions(width - 60, 20, 40, 20)
                .tooltip(Tooltip.of(Text.literal("You come back to the general page")))
                .build();
        addDrawableChild(backButton);

        // Button zum Erstellen eines neuen Teams in der unteren rechten Ecke
        int buttonWidth = 150;
        int buttonHeight = 20;
        int xPos = this.width - buttonWidth - 10; // 10 Pixel Abstand zum Rand
        int yPos = this.height - buttonHeight - 10;

        createTeamButton = ButtonWidget.builder(Text.literal("Create a new Team"), button -> {
                    addNewTeamObject();
                })
                .dimensions(xPos, yPos, buttonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.literal("Click here to create a new Team")))
                .build();
        addDrawableChild(createTeamButton);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (TeamCategoryButton teamObject : teamObjects) {

            if (!isMouseOverButton(mouseX, mouseY, teamObject.textField)) {
                teamObject.textField.setFocused(false);
                teamObject.textField.setEditable(false);
                saveTeamObjects();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // Überprüft, ob die Enter-Taste gedrückt wurde
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            for (TeamCategoryButton teamObject : teamObjects) {
                teamObject.textField.setFocused(false);
                teamObject.textField.setEditable(false);
                saveTeamObjects();
            }
            return true; // Blockiert weitere Verarbeitung, wenn gewünscht
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    private boolean isMouseOverButton(double mouseX, double mouseY,TextFieldWidget button) {

        int buttonX = button.getX();
        int buttonY = button.getY();
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();

        // Überprüfen, ob die Maus sich innerhalb der Button-Grenzen befindet
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth
                && mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
    }


    // Methode, um ein neues Team-Objekt zu erstellen und in der Liste zu speichern
    private void addNewTeamObject() {
        int newYPos = 50 + teamObjects.size() * spacing; // Berechne die Y-Position basierend auf der Anzahl der Objekte
        TeamCategoryButton newTeamObject = new TeamCategoryButton(newYPos, "New Team " + (teamObjects.size() + 1),this,null);
        teamObjects.add(newTeamObject); // Füge das neue Team zur Liste hinzu
        updateTeamObjectPositions(); // Aktualisiere die Y-Positionen der Team-Objekte
    }

    // Methode, um die Y-Positionen der Team-Objekte basierend auf ihrer Position in der Liste neu zu sortieren
    private void updateTeamObjectPositions() {
        for (int i = 0; i < teamObjects.size(); i++) {
            int newYPos = 50 + i * spacing; // Neue Y-Position basierend auf dem Index
            teamObjects.get(i).setYPos(newYPos); // Setze die neue Y-Position für jedes Team-Objekt
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        float scaleTitle = 2.0f;
        MatrixStack matrices = context.getMatrices();

        // Skalierung anwenden
        matrices.push(); // Speichert den aktuellen Zustand des MatrixStack
        matrices.scale(scaleTitle, scaleTitle, scaleTitle); // Skalierung anwenden

        // Berechnung der Position für den zentrierten Text unter Berücksichtigung der Skalierung
        int scaledWidth = (int) ((float) width / 2 / scaleTitle); // Bildschirmmitte bei skalierter Größe
        int scaledY = (int) (20 / scaleTitle); // Y-Position ebenfalls skalieren

        // Zeichne den skalierten, zentrierten Text
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("TeamManager"), scaledWidth, scaledY, 11141290);
        matrices.pop(); // Skalierung zurücksetzen


    }

    // Methode zum Speichern der Team-Objekte
    private void saveTeamObjects() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config/team_objects.txt"))) {
            for (TeamCategoryButton teamObject : teamObjects) {
                String colorFormatting = teamObject.getTeamColor().getName();
                writer.write(teamObject.getTeamName() + ";" + colorFormatting);

                // Speichere die Unterobjekte als Komma-separierte Liste
                String subObjectData = String.join(",", teamObject.getSubObjects());
                writer.write(";" + subObjectData);

                writer.newLine();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    // Methode zum Laden der Team-Objekte
    private void loadTeamObjects() {
        try (BufferedReader reader = new BufferedReader(new FileReader("config/team_objects.txt"))) {
            String line;
            teamObjects.clear();
            int yPos = 50;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    String teamName = parts[0];
                    String colorName = parts[1];
                    Formatting teamColor = Formatting.byName(colorName.toUpperCase());

                    // Erstelle ein neues Team-Objekt
                    TeamCategoryButton teamObject = new TeamCategoryButton(yPos, teamName, this, teamColor);

                    // Lade die Unterobjekte
                    if (parts.length >= 3) {
                        String[] subObjectArray = parts[2].split(",");
                        teamObject.setSubObjects(Arrays.asList(subObjectArray));
                    }

                    teamObjects.add(teamObject);
                    yPos += spacing;
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }



    public class TeamDetail_Screen extends Screen {
        private final TeamCategoryButton teamObject;
        private final List<SubObject> subObjects; // Liste der SubObject-Instanzen
        private ButtonWidget addButton;
        private final int spacing = 30; // Abstand zwischen den Unterobjekten

        public TeamDetail_Screen(TeamCategoryButton teamObject) {
            super(Text.literal("Team Detail"));
            this.teamObject = teamObject;
            this.subObjects = new ArrayList<>();
        }

        @Override
        protected void init() {
            backButton = ButtonWidget.builder(Text.literal("Back"), button -> {
                        teamObject.setSubObjects(subObjects.stream().map(subObject -> subObject.getTextField().getText()).toList());
                        saveTeamObjects();
                        MinecraftClient.getInstance().setScreen(new TeamManager_Screen());
                    })
                    .dimensions(width - 60, 20, 40, 20)
                    .tooltip(Tooltip.of(Text.literal("You come back to the TeamManager page")))
                    .build();
            addDrawableChild(backButton);

            // [+] Button hinzufügen
            addButton = ButtonWidget.builder(Text.literal("+"), button -> {
                addSubObject(); // Methode, um ein neues Unterobjekt hinzuzufügen
            }).dimensions((this.width - 40) / 2, 50, 20, 20).build();
            addDrawableChild(addButton);




            // Lade existierende Unterobjekte
            loadSubObjects();
            updateSubObjectPositions();
        }

        private void addSubObject() {
            int newYPos = 50 + subObjects.size() * spacing;

            // Füge ein neues Sub-Objekt hinzu
            SubObject newSubObject = new SubObject(this, (this.width - 200) / 2, newYPos, 200);
            subObjects.add(newSubObject);

            updateSubObjectPositions();
        }

        private void updateSubObjectPositions() {
            for (int i = 0; i < subObjects.size(); i++) {
                int newYPos = 50 + i * spacing;
                subObjects.get(i).updatePosition((this.width - 200) / 2, newYPos, 200);
            }
            addButton.setY(50 + subObjects.size() * spacing); // Setze den [+] Button unter das letzte Unterobjekt
        }

        private void loadSubObjects() {
            for (String subObjectText : teamObject.getSubObjects()) {
                addSubObject();
                subObjects.getLast().getTextField().setText(subObjectText); // Setze Text in das Textfeld
            }
        }
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            //Headline
            super.render(context, mouseX, mouseY, delta);
            float scaleTitle = 3.0f;
            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.scale(scaleTitle, scaleTitle, scaleTitle);
            int scaledWidth = (int) ((float) width / 2 / scaleTitle);
            int scaledY = (int) (20 / scaleTitle);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(teamObject.getTeamName()), scaledWidth, scaledY, getColorInt(getTeamColor(teamObject.getTeamName())));
            matrices.pop();

            for (int i = 0; i < subObjects.size(); i++) {
                int newYPos = 50 + i * spacing;
                //subObjects.get(i).drawPlayerIcon(matrices);
                if(subObjects.get(i).getGameProfileByName() != null){
                    matrices.push();
                    matrices.scale(0.625f, 0.625f, 0.625f);
                    context.drawTexture(subObjects.get(i).getGameProfileByName(),(int)((width / 2 - 125) * 1.6),(int)(subObjects.get(i).textField.getY() * 1.6),32,32,32,32);
                    matrices.pop();
                }
                else{
                    matrices.push();
                    matrices.scale(0.625f, 0.625f, 0.625f);
                    context.drawTexture(Identifier.of(("minecraft:textures/entity/player/wide/steve.png")),(int)((width / 2 - 125) * 1.6),(int)(subObjects.get(i).textField.getY() * 1.6),32,32,32,32);
                    matrices.pop();
                }
                //System.out.println("Player: " + subObjects.get(i).textField.getText() + " | GameProfile: " + subObjects.get(i).getGameProfileByName());
                assert MinecraftClient.getInstance().player != null;
                //MinecraftClient.getInstance().player.sendMessage(Text.of("Player: " + subObjects.get(i).textField.getText() + " | GameProfile: " + subObjects.get(i).getGameProfileByName()));
            }


        }

        public static int getColorInt(Formatting formatting) {
            // Prüfen, ob die `Formatting`-Farbe eine eigene Farbe besitzt
            TextColor color = TextColor.fromFormatting(formatting);
            if (color != null) {
                return color.getRgb();
            }
            // Rückgabe einer Standardfarbe (z.B. Weiß), falls keine Farbe definiert ist
            return 0xFFFFFF;
        }

        public static Formatting getTeamColor(String teamName){
            Formatting x = null;
            try (BufferedReader reader = new BufferedReader(new FileReader("config/team_objects.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        String teamName_ = parts[0];
                        String colorName = parts[1];
                        if(Objects.equals(teamName_, teamName)){
                            x = Formatting.byName(colorName.toUpperCase());
                        }

                    }
                }
            }
            catch (IOException e) {
            }
            return x;
        }
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            for (SubObject subObject : subObjects) {

                if (!isMouseOverButton(mouseX, mouseY, subObject.textField)) {
                    subObject.textField.setFocused(false);
                    subObject.textField.setEditable(false);
                    saveTeamObjects();
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        // Überprüft, ob die Enter-Taste gedrückt wurde
        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                for (SubObject subObject : subObjects) {
                    subObject.textField.setFocused(false);
                    subObject.textField.setEditable(false);
                    saveTeamObjects();
                }
                return true; // Blockiert weitere Verarbeitung, wenn gewünscht
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        private boolean isMouseOverButton(double mouseX, double mouseY,TextFieldWidget button) {

            int buttonX = button.getX();
            int buttonY = button.getY();
            int buttonWidth = button.getWidth();
            int buttonHeight = button.getHeight();

            // Überprüfen, ob die Maus sich innerhalb der Button-Grenzen befindet
            return mouseX >= buttonX && mouseX <= buttonX + buttonWidth
                    && mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
        }



        @Override
        public void close() {
            teamObject.setSubObjects(subObjects.stream().map(subObject -> subObject.getTextField().getText()).toList());
            saveTeamObjects();
            super.close();
        }

        public class SubObject {
            private final TextFieldWidget textField;
            private final ButtonWidget editButton;
            private ButtonWidget deleteButton;


            public SubObject(TeamDetail_Screen screen, int x, int y, int width) {
                // Erstelle das Textfeld
                this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, x, y, width, 20, Text.literal("Player " + subObjects.size()));
                this.textField.setEditable(false); // Initial nicht bearbeitbar
                this.textField.setText("Player " + subObjects.size());
                addDrawableChild(this.textField);

                // Erstelle den Bearbeiten-Button
                this.editButton = ButtonWidget.builder(Text.literal("✎"), button -> {
                    this.textField.setEditable(true);
                    this.textField.setFocused(true);
                }).dimensions(x - 40, y, 20, 20).build();
                addDrawableChild(this.editButton);

                // Erstelle den Löschen-Button
                this.deleteButton = ButtonWidget.builder(Text.literal("🗑"), button -> {
                    remove(this.textField);
                    remove(this.editButton);
                    remove(this.deleteButton);
                    subObjects.remove(this);
                    updateSubObjectPositions();
                }).dimensions(x - 80, y, 20, 20).build();
                addDrawableChild(this.deleteButton);


            }

            // Aktualisiert die Position aller Elemente (Textfeld, Buttons)
            public void updatePosition(int x, int y, int width) {
                this.textField.setX(x);
                this.textField.setY(y);
                this.textField.setWidth(width);

                this.editButton.setX(x + 205);
                this.editButton.setY(y);

                this.deleteButton.setX(x + 230);
                this.deleteButton.setY(y);
            }

            public TextFieldWidget getTextField() {
                return this.textField;
            }


            private Identifier getGameProfileByName() {
                String playerName = textField.getText();
                //GameProfile
                GameProfile x = null;
                MinecraftClient client = MinecraftClient.getInstance();
                for (PlayerListEntry playerListEntry : Objects.requireNonNull(client.getNetworkHandler()).getPlayerList()) {
                    GameProfile profile = playerListEntry.getProfile();
                    if (profile.getName().equals(playerName)) {
                        x = profile;
                    }
                }
                if(x==null){
                    return null;
                }
                // Hole den PlayerSkinProvider für die Textur verwaltung
                PlayerSkinProvider skinProvider = MinecraftClient.getInstance().getSkinProvider();

                // Abrufen der Skin-Textur für das GameProfil
                SkinTextures skinTexture = skinProvider.getSkinTextures(x);
                if (skinTexture != null) {
                    Identifier skinTexture_ = skinProvider.getSkinTextures(x).texture();
                    return skinTexture_;
                }

                return null; // Wenn keine Textur gefunden wurde
            }
        }
    }




    public class TeamCategoryButton {
        // Neue Attribute für Unterobjekte
        private List<String> subObjects = new ArrayList<>();

        // Getter und Setter für Unterobjekte
        public List<String> getSubObjects() {
            return subObjects;
        }

        public void setSubObjects(List<String> subObjects) {
            this.subObjects = subObjects;
        }
        //Objekte
        private ButtonWidget deleteButton;
        private ButtonWidget editButton;
        private ButtonWidget dropdownButton;
        private ButtonWidget colorButton;
        private final TextFieldWidget textField;
        private final TeamManager_Screen parentScreen;
        //Attribute
        private Formatting teamColor = Formatting.DARK_RED;
        private int yPos; // Y-Position für das Layout
        // Liste von vordefinierten Farben (einfaches Farbauswahl-System)
        private final Formatting[] colors ={
                Formatting.DARK_RED,   // Dunkelrot
                Formatting.RED,        // Rot
                Formatting.GOLD,       // Gold
                Formatting.YELLOW,     // Gelb
                Formatting.DARK_GREEN, // Dunkelgrün
                Formatting.GREEN,      // Grün
                Formatting.AQUA,       // Aqua
                Formatting.DARK_AQUA,  // Dunkel Aqua
                Formatting.DARK_BLUE,  // Dunkelblau
                Formatting.BLUE,       // Blau
                Formatting.LIGHT_PURPLE, // Lila (hell)
                Formatting.DARK_PURPLE,  // Dunkellila
                Formatting.WHITE,      // Weiß
                Formatting.GRAY,       // Grau
                Formatting.DARK_GRAY,  // Dunkelgrau
                Formatting.BLACK       // Schwarz
        };
        private int currentColorIndex = 0;

        public TeamCategoryButton(int yPos, String defaultText,TeamManager_Screen tM_screen,Formatting teamColor_) {
            this.yPos = yPos; // Setze die Y-Position
            parentScreen = tM_screen;
            int textFieldWidth = 200;
            int textFieldHeight = 20;
            this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width/2 - 100, yPos, textFieldWidth, textFieldHeight, Text.literal(defaultText));
            this.textField.setEditable(false); // Initial nicht bearbeitbar
            this.textField.setText(defaultText);
            this.textField.setMaxLength(30); // Maximale Zeichenanzahl
            this.textField.setFocused(false);
            this.textField.setUneditableColor(11184810);
            this.textField.setEditableColor(16777215);
            if(teamColor_ != null) {
                teamColor = teamColor_;
                currentColorIndex = getColorIndex(teamColor);
            }
            // Mülleimer-Button
            int buttonSize = 20;
            this.deleteButton = ButtonWidget.builder(Text.literal("🗑"), button -> {
                        // Logik für das Löschen des Objekts
                        System.out.println("Vor TeamRemove: " + teamObjects.indexOf(this));
                        teamObjects.remove(this); // Entferne dieses Objekt aus der Liste
                        System.out.println("NachTeamRemove: " + teamObjects.indexOf(this));
                        updateTeamObjectPositions(); // Aktualisiere die Y-Positionen nach dem Löschen
                        //entferne Objekte
                        parentScreen.remove(editButton);
                        parentScreen.remove(dropdownButton);
                        parentScreen.remove(textField);
                        parentScreen.remove(deleteButton);
                        parentScreen.remove(colorButton);
                    })
                    .dimensions(width/2 - 125, yPos, buttonSize, buttonSize) // Links vom Textfeld //20
                    .build();

            // Farb-Auswahl-Knopf (zwischen Textfeld und Edit-Button)
            int colorButtonXPos = width/2 - 100 + textFieldWidth + 5;
            //parentScreen.renderColorButton(,this.colorButton,teamColor);
            this.colorButton = ButtonWidget.builder(Text.literal(" "), button -> {
                // Ändere die Farbe
                currentColorIndex = (currentColorIndex + 1) % colors.length; // Zyklisches Durchgehen der Farben
                teamColor = colors[currentColorIndex]; // Setze die neue Farbe
                colorButton.setMessage(Text.literal("🟥").setStyle(Style.EMPTY.withColor(teamColor))); // Leere Nachricht, Farbe zeigt sich über Hintergrund
            }).dimensions(colorButtonXPos, yPos, buttonSize, buttonSize).build();
            colorButton.setMessage(Text.literal("🟥").setStyle(Style.EMPTY.withColor(teamColor)));

            // Stift-Button (Bearbeiten)
            int editButtonXPos = colorButtonXPos + buttonSize + 5;
            this.editButton = ButtonWidget.builder(Text.literal("✎"), button -> {
                        // Logik zum Bearbeiten des Textes
                        this.textField.setEditable(true);
                        this.textField.setFocused(true);

                    })
                    .dimensions(editButtonXPos, yPos, buttonSize, buttonSize)
                    .build();

            // Ausklapp-Button
            int dropdownButtonXPos = editButtonXPos + buttonSize + 5;
            this.dropdownButton = ButtonWidget.builder(Text.literal("▼"), button -> {
                        // Logik zum Ausklappen des Inhalts
                        System.out.println("Dropdown clicked");
                        MinecraftClient.getInstance().setScreen(new TeamDetail_Screen(this)); // Neuer Screen wird geöffnet
                    })
                    .dimensions(dropdownButtonXPos, yPos, buttonSize, buttonSize)
                    .build();

            // Füge die Widgets zur Oberfläche hinzu
            addDrawableChild(deleteButton);
            addDrawableChild(editButton);
            addDrawableChild(dropdownButton);
            addDrawableChild(textField);
            addDrawableChild(colorButton);
        }

        // Methode, um Y-Position zu ändern (wenn notwendig)
        public void setYPos(int yPos) {
            this.yPos = yPos;
            textField.setY(yPos);
            deleteButton.setY(yPos);
            editButton.setY(yPos);
            colorButton.setY(yPos);
            dropdownButton.setY(yPos);
        }

        public String getTeamName() {
            String old_text = textField.getText();
            String new_text = (old_text.replace("@","a")).replace(";",",");
            textField.setText(new_text);
            return textField.getText(); // Gibt den Text aus dem Textfeld zurück
        }
        public Formatting getTeamColor() {
            return this.teamColor;
        }
        private int getColorIndex(Formatting colorToFind) {
            for (int i = 0; i < colors.length; i++) {
                if (colors[i] == colorToFind) { // Vergleich der Farbe
                    return i; // Gibt den Index zurück, wenn die Farbe gefunden wurde
                }
            }
            return -1; // Gibt -1 zurück, wenn die Farbe nicht gefunden wurde
        }







    }
}