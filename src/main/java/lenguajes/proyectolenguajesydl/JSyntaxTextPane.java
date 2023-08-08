package lenguajes.proyectolenguajesydl;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 * This class is a prototype for a syntax highlighter for java code.
 * It highlights common java keywords, boolean values and it highlights digits in the text.
 * 
 * Limitations of the current prototype:
 * -It does not highlight comments
 * -It does not highlight method calls
 * -It does not highlight objects that are not a part of common java keywords
 * -It does not have intellisense autosuggestion
 * 
 * Addendum:
 * Even though this syntax highlighter is designed for java code, {@link #initializeSyntaxHighlighter()} can be modified to highlight any other programming language or keywords
 */
public class JSyntaxTextPane extends JTextPane {

    // Default Styles
    final StyleContext cont = StyleContext.getDefaultStyleContext();
    AttributeSet defaultForeground = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.white);
    AttributeSet defaultNumbers = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.cyan);

    public JSyntaxTextPane () {
        // Styler
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, getDeveloperShortcuts(str), a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        // Colors words in appropriate style, if nothing matches, make it default black
                        boolean matchFound = false;

                        for (KeyWord keyWord : keyWords) {
                            if (text.substring(wordL, wordR).matches("(\\W)*("+keyWord.getWords()+")")) {
                                setCharacterAttributes(wordL, wordR - wordL, keyWord.getColorAttribute(), false);
                                matchFound = true;
                            }
                        }

                        // Highlight numbers
                        if (text.substring(wordL, wordR).matches("\\W\\d[\\d]*")) {
                            setCharacterAttributes(wordL, wordR - wordL, defaultNumbers, false);
                            matchFound = true;
                        }

                        // ================ ANY ADDITIONAL HIGHLIGHTING LOGIC MAY BE ADDED HERE
                        // Ideas: highlighting a comment; highlighting method calls;

                        // ================

                        // If no match found, make text default colored
                        if(!matchFound) {
                            setCharacterAttributes(wordL, wordR - wordL, defaultForeground, false);
                        }

                        wordL = wordR;
                    }

                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                // Colors words in appropriate style, if nothing matches, make it default black
                boolean matchFound = false;

                for (KeyWord keyWord : keyWords) {
                    if (text.substring(before, after).matches("(\\W)*("+keyWord.getWords()+")")) {
                        setCharacterAttributes(before, after - before, keyWord.getColorAttribute(), false);
                        matchFound = true;
                    } 

                    // Highlight numbers
                    if (text.substring(before, after).matches("\\W\\d[\\d]*")) {
                        setCharacterAttributes(before, after - before, defaultNumbers, false);
                        matchFound = true;
                    }

                    // ================ ANY ADDITIONAL HIGHLIGHTING LOGIC MAY BE ADDED HERE
                    // Ideas: highlighting a comment; highlighting method calls;

                    // ================

                    if(!matchFound) {
                        setCharacterAttributes(before, after - before, defaultForeground, false);
                    }
                }
            }
        };

        setStyledDocument(doc);

        // SET DEFAULT TAB SIZE
        setTabSize(40);

        // THIS PART APPLIES DEFAULT SYNTAX HIGHLIGHTER BEHAVIOUR
        initializeSyntaxHighlighter();
    }

    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * Shortcuts, when letter is typed, it will produce additional strings specified inside of this method
     * 
     * @param str
     * @return
     */
    private String getDeveloperShortcuts(String str) {
        // Add ending parenthesis when it is open
        if(str.equals("(")) {
            return "()";
        }

        // Add ending braces when it is open
        if(str.equals("{")) {
            return "{\n\n};";
        }

        return str;
    }

    /**
     * Sets size of space produced when user presses Tab button
     * 
     * @param tabSize
     */
    public void setTabSize(int tabSize) {
        // Once tab count exceed x times, it will make a small space only
        int maxTabsPerRow = 10;

        TabStop[] tabs = new TabStop[maxTabsPerRow];
        for(int i = 0; i < maxTabsPerRow; i++) {
            tabs[i] = new TabStop(tabSize*(i+1), TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        }

        TabSet tabset = new TabSet(tabs);

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
        StyleConstants.TabSet, tabset);
        setParagraphAttributes(aset, false);
    }

    /**
     * Adds a key word or keywords that will be colored in the JTextPane
     * 
     * @param color - color of the words
     * @param words - words that need to be colored
     */
    public void addKeyWord(Color color, String ...words) {
        keyWords.add(new KeyWord(color, words));
    }

    ArrayList<KeyWord> keyWords = new ArrayList<KeyWord>();

    /**
     * Holds information about list of words that need to be colored in a specific color
     * 
     */
    class KeyWord {
        Color color;
        String[] words;

        /**
         * Instantiates a new key word object that holds a list of words that need to be colored.
         *
         * @param color the color
         * @param words the words
         */
        public KeyWord(Color color, String...words) {
            this.color = color;
            this.words = words;
        }

        public String getWords() {
            String text = "";

            for (int i = 0; i < words.length; i++) {
                if(i != words.length-1) {
                    text = text + words[i] + "|";
                } else {
                    text = text + words[i];
                }
            }

            return text;
        }

        public AttributeSet getColorAttribute() {
            StyleContext cont = StyleContext.getDefaultStyleContext();
            return cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, color);
        }

    }

    /**
     * Sets color of all integers
     * 
     * @param color
     */
    public void setIntegerColours(Color color) {
        defaultNumbers = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, color);

    }

    /**
     * Sets color of non-keywords 
     * 
     * @param color
     */
    public void setDefaultTextColour(Color color) {
        defaultForeground = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, color);
    }

    /**
     * Initializes rules by which textpane should be coloring text
     */
    public void initializeSyntaxHighlighter() {
        // Set background color
        setBackground(Color.black);

        // Java keywords
        addKeyWord(Color.pink,
                "abstract",
                "continue",
                "for",
                "new",
                "switch",
                "assert",
                "default",
                "goto",
                "package",
                "synchronized",
                "do",
                "if",
                "private",
                "this",
                "break",
                "double",
                "implements",
                "protected",
                "throw",
                "else",
                "import",
                "public",
                "throws",
                "case",
                "enum",
                "instanceof",
                "return",
                "transient",
                "catch",
                "extends",
                "short",
                "try",
                "char",
                "final",
                "interface",
                "static",
                "void",
                "class",
                "finally",
                "strictfp",
                "volatile",
                "const",
                "native",
                "super",
                "while"
                );

        // Developer's preference
        addKeyWord(Color.green,
                "true"
                );

        addKeyWord(Color.red,
                "false"
                );

        addKeyWord(Color.red, 
                "!"
                );

        // Java Variables
        addKeyWord(Color.yellow,
                "String",
                "byte", "Byte",
                "short", "Short",
                "int", "Integer",
                "long", "Long",
                "float", "Float",
                "double", "Double",
                "char", "Character",
                "boolean", "Boolean");
    }

    /**
     * Demo for testing purposes
     */
    public static void main(String[] args) {
        // Our Component
        JSyntaxTextPane textPane = new JSyntaxTextPane();

        textPane.setText("public class Test {\r\n"
                + " int age = 18;\r\n"
                + " String name = \"Gerald\";\r\n"
                + " Long life = 100.50;\r\n"
                + " boolean alive = true;\r\n"
                + " boolean happy = false;\r\n"
                + " \r\n"
                + " // Comment Example\r\n"
                + " public static void shout(int loudness) {\r\n"
                + "     System.out.println(loudness);\r\n"
                + " };\r\n"
                + "\r\n"
                + "};");

        // JFrame
        JFrame frame = new JFrame("Test");
        frame.getContentPane().add(textPane);
        frame.pack();
        frame.setSize(350, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

} 