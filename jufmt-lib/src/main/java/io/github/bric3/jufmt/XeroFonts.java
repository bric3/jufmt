package io.github.bric3.jufmt;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public enum XeroFonts implements Figlet.FontSpec {
    _1_Row("1Row", "1Row.flf"),
    _3_D("3-D", "3-D.flf"),
    _3d("3d", "3d.flf"),
    _3D_Diagonal("3D Diagonal", "3D Diagonal.flf"),
    _3D_ASCII("3D-ASCII", "3D-ASCII.flf"),
    _3D_diagonal("3d_diagonal", "3d_diagonal.flf"),
    _3_PER_5("3x5", "3x5.flf"),
    _4_Max("4Max", "4Max.flf"),
    _5_Line_Oblique("5 Line Oblique", "5 Line Oblique.flf"),
    _5_lineoblique("5lineoblique", "5lineoblique.flf"),
    Acrobatic("Acrobatic", "Acrobatic.flf"),
    Alligator("Alligator", "Alligator.flf"),
    Alligator2("Alligator2", "Alligator2.flf"),
    alligator3("alligator3", "alligator3.flf"),
    Alpha("Alpha", "Alpha.flf"),
    Alphabet("Alphabet", "Alphabet.flf"),
    AMC_AAA01("AMC AAA01", "AMC AAA01.flf"),
    AMC_Neko("AMC Neko", "AMC Neko.flf"),
    AMC_Razor("AMC Razor", "AMC Razor.flf"),
    AMC_Razor2("AMC Razor2", "AMC Razor2.flf"),
    AMC_Slash("AMC Slash", "AMC Slash.flf"),
    AMC_Slider("AMC Slider", "AMC Slider.flf"),
    AMC_Thin("AMC Thin", "AMC Thin.flf"),
    AMC_Tubes("AMC Tubes", "AMC Tubes.flf"),
    AMC_Untitled("AMC Untitled", "AMC Untitled.flf"),
    amc3line("amc3line", "amc3line.flf"),
    amc3liv1("amc3liv1", "amc3liv1.flf"),
    AMC_3_Line("AMC 3 Line", "AMC 3 Line.flf"),
    AMC_3_Liv1("AMC 3 Liv1", "AMC 3 Liv1.flf"),
    amcaaa01("amcaaa01", "amcaaa01.flf"),
    amcneko("amcneko", "amcneko.flf"),
    amcrazo2("amcrazo2", "amcrazo2.flf"),
    amcrazor("amcrazor", "amcrazor.flf"),
    amcslash("amcslash", "amcslash.flf"),
    amcslder("amcslder", "amcslder.flf"),
    amcthin("amcthin", "amcthin.flf"),
    amctubes("amctubes", "amctubes.flf"),
    amcun1("amcun1", "amcun1.flf"),
    ANSI_Regular("ANSI Regular", "ANSI Regular.flf"),
    ANSI_Shadow("ANSI Shadow", "ANSI Shadow.flf"),
    Arrows("Arrows", "Arrows.flf"),
    ASCII_New_Roman("ASCII New Roman", "ASCII New Roman.flf"),
    ascii9("ascii9", "ascii9.tlf"),
    ascii12("ascii12", "ascii12.tlf"),
    ascii_new_roman("ascii_new_roman", "ascii_new_roman.flf"),
    Avatar("Avatar", "Avatar.flf"),
    B1FF("B1FF", "B1FF.flf"),
    Banner("Banner", "Banner.flf"),
    Banner3("Banner3", "Banner3.flf"),
    Banner3_D("Banner3-D", "Banner3-D.flf"),
    Banner4("Banner4", "Banner4.flf"),
    Barbwire("Barbwire", "Barbwire.flf"),
    Basic("Basic", "Basic.flf"),
    Bear("Bear", "Bear.flf"),
    Bell("Bell", "Bell.flf"),
    Benjamin("Benjamin", "Benjamin.flf"),
    Big("Big", "Big.flf"),
    Big_Chief("Big Chief", "Big Chief.flf"),
    Big_Money_ne("Big Money-ne", "Big Money-ne.flf"),
    Big_Money_nw("Big Money-nw", "Big Money-nw.flf"),
    Big_Money_se("Big Money-se", "Big Money-se.flf"),
    Big_Money_sw("Big Money-sw", "Big Money-sw.flf"),
    bigascii9("bigascii9", "bigascii9.tlf"),
    bigascii12("bigascii12", "bigascii12.tlf"),
    bigchief("bigchief", "bigchief.flf"),
    Bigfig("Bigfig", "Bigfig.flf"),
    bigmono9("bigmono9", "bigmono9.tlf"),
    bigmono12("bigmono12", "bigmono12.tlf"),
    Binary("Binary", "Binary.flf"),
    Block("Block", "Block.flf"),
    Blocks("Blocks", "Blocks.flf"),
    Bloody("Bloody", "Bloody.flf"),
    Bolger("Bolger", "Bolger.flf"),
    Braced("Braced", "Braced.flf"),
    Bright("Bright", "Bright.flf"),
    Broadway("Broadway", "Broadway.flf"),
    Broadway_KB("Broadway KB", "Broadway KB.flf"),
    broadway_kb("broadway_kb", "broadway_kb.flf"),
    Bubble("Bubble", "Bubble.flf"),
    Bulbhead("Bulbhead", "Bulbhead.flf"),
    calgphy2("calgphy2", "calgphy2.flf"),
    Caligraphy("Caligraphy", "Caligraphy.flf"),
    Caligraphy2("Caligraphy2", "Caligraphy2.flf"),
    Calvin_S("Calvin S", "Calvin S.flf"),
    Cards("Cards", "Cards.flf"),
    Catwalk("Catwalk", "Catwalk.flf"),
    Chiseled("Chiseled", "Chiseled.flf"),
    Chunky("Chunky", "Chunky.flf"),
    circle("circle", "circle.tlf"),
    Coinstak("Coinstak", "Coinstak.flf"),
    Cola("Cola", "Cola.flf"),
    Colossal("Colossal", "Colossal.flf"),
    Computer("Computer", "Computer.flf"),
    Contessa("Contessa", "Contessa.flf"),
    Contrast("Contrast", "Contrast.flf"),
    cosmic("cosmic", "cosmic.flf"),
    Cosmike("Cosmike", "Cosmike.flf"),
    Crawford("Crawford", "Crawford.flf"),
    Crawford2("Crawford2", "Crawford2.flf"),
    Crazy("Crazy", "Crazy.flf"),
    Cricket("Cricket", "Cricket.flf"),
    Cursive("Cursive", "Cursive.flf"),
    Cyberlarge("Cyberlarge", "Cyberlarge.flf"),
    Cybermedium("Cybermedium", "Cybermedium.flf"),
    Cybersmall("Cybersmall", "Cybersmall.flf"),
    Cygnet("Cygnet", "Cygnet.flf"),
    DANC4("DANC4", "DANC4.flf"),
    Dancing_Font("Dancing Font", "Dancing Font.flf"),
    dancingfont("dancingfont", "dancingfont.flf"),
    Decimal("Decimal", "Decimal.flf"),
    Def_Leppard("Def Leppard", "Def Leppard.flf"),
    defleppard("defleppard", "defleppard.flf"),
    Delta_Corps_Priest_1("Delta Corps Priest 1", "Delta Corps Priest 1.flf"),
    Diamond("Diamond", "Diamond.flf"),
    Diet_Cola("Diet Cola", "Diet Cola.flf"),
    dietcola("dietcola", "dietcola.flf"),
    Digital("Digital", "Digital.flf"),
    Doh("Doh", "Doh.flf"),
    Doom("Doom", "Doom.flf"),
    DOS_Rebel("DOS Rebel", "DOS Rebel.flf"),
    dosrebel("dosrebel", "dosrebel.flf"),
    Dot_Matrix("Dot Matrix", "Dot Matrix.flf"),
    dotmatrix("dotmatrix", "dotmatrix.flf"),
    Double("Double", "Double.flf"),
    Double_Shorts("Double Shorts", "Double Shorts.flf"),
    doubleshorts("doubleshorts", "doubleshorts.flf"),
    Dr_Pepper("Dr Pepper", "Dr Pepper.flf"),
    drpepper("drpepper", "drpepper.flf"),
    DWhistled("DWhistled", "DWhistled.flf"),
    Efti_Chess("Efti Chess", "Efti Chess.flf"),
    Efti_Font("Efti Font", "Efti Font.flf"),
    Efti_Italic("Efti Italic", "Efti Italic.flf"),
    Efti_Piti("Efti Piti", "Efti Piti.flf"),
    Efti_Robot("Efti Robot", "Efti Robot.flf"),
    Efti_Wall("Efti Wall", "Efti Wall.flf"),
    Efti_Water("Efti Water", "Efti Water.flf"),
    eftichess("eftichess", "eftichess.flf"),
    eftifont("eftifont", "eftifont.flf"),
    eftipiti("eftipiti", "eftipiti.flf"),
    eftirobot("eftirobot", "eftirobot.flf"),
    eftitalic("eftitalic", "eftitalic.flf"),
    eftiwall("eftiwall", "eftiwall.flf"),
    eftiwater("eftiwater", "eftiwater.flf"),
    Electronic("Electronic", "Electronic.flf"),
    Elite("Elite", "Elite.flf"),
    emboss("emboss", "emboss.tlf"),
    emboss2("emboss2", "emboss2.tlf"),
    Epic("Epic", "Epic.flf"),
    Fender("Fender", "Fender.flf"),
    Filter("Filter", "Filter.flf"),
    Fire_Font_k("Fire Font-k", "Fire Font-k.flf"),
    Fire_Font_s("Fire Font-s", "Fire Font-s.flf"),
    fire_font_k("fire_font-k", "fire_font-k.flf"),
    fire_font_s("fire_font-s", "fire_font-s.flf"),
    Flipped("Flipped", "Flipped.flf"),
    Flower_Power("Flower Power", "Flower Power.flf"),
    flowerpower("flowerpower", "flowerpower.flf"),
    Four_Tops("Four Tops", "Four Tops.flf"),
    fourtops("fourtops", "fourtops.flf"),
    Fraktur("Fraktur", "Fraktur.flf"),
    Fun_Face("Fun Face", "Fun Face.flf"),
    Fun_Faces("Fun Faces", "Fun Faces.flf"),
    funface("funface", "funface.flf"),
    funfaces("funfaces", "funfaces.flf"),
    future("future", "future.tlf"),
    Fuzzy("Fuzzy", "Fuzzy.flf"),
    Georgi16("Georgi16", "Georgi16.flf"),
    Georgia11("Georgia11", "Georgia11.flf"),
    Ghost("Ghost", "Ghost.flf"),
    Ghoulish("Ghoulish", "Ghoulish.flf"),
    Glenyn("Glenyn", "Glenyn.flf"),
    Goofy("Goofy", "Goofy.flf"),
    Gothic("Gothic", "Gothic.flf"),
    Graceful("Graceful", "Graceful.flf"),
    Gradient("Gradient", "Gradient.flf"),
    Graffiti("Graffiti", "Graffiti.flf"),
    Greek("Greek", "Greek.flf"),
    Heart_Left("Heart Left", "Heart Left.flf"),
    Heart_Right("Heart Right", "Heart Right.flf"),
    heart_left("heart_left", "heart_left.flf"),
    heart_right("heart_right", "heart_right.flf"),
    henry3d("henry3d", "henry3d.flf"),
    Henry_3D("Henry 3D", "Henry 3D.flf"),
    Hex("Hex", "Hex.flf"),
    Hieroglyphs("Hieroglyphs", "Hieroglyphs.flf"),
    Hollywood("Hollywood", "Hollywood.flf"),
    Horizontal_Left("Horizontal Left", "Horizontal Left.flf"),
    Horizontal_Right("Horizontal Right", "Horizontal Right.flf"),
    horizontalleft("horizontalleft", "horizontalleft.flf"),
    horizontalright("horizontalright", "horizontalright.flf"),
    ICL_1900("ICL-1900", "ICL-1900.flf"),
    Impossible("Impossible", "Impossible.flf"),
    Invita("Invita", "Invita.flf"),
    Isometric1("Isometric1", "Isometric1.flf"),
    Isometric2("Isometric2", "Isometric2.flf"),
    Isometric3("Isometric3", "Isometric3.flf"),
    Isometric4("Isometric4", "Isometric4.flf"),
    Italic("Italic", "Italic.flf"),
    Ivrit("Ivrit", "Ivrit.flf"),
    Jacky("Jacky", "Jacky.flf"),
    Jazmine("Jazmine", "Jazmine.flf"),
    Jerusalem("Jerusalem", "Jerusalem.flf"),
    JS_Block_Letters("JS Block Letters", "JS Block Letters.flf"),
    JS_Bracket_Letters("JS Bracket Letters", "JS Bracket Letters.flf"),
    JS_Capital_Curves("JS Capital Curves", "JS Capital Curves.flf"),
    JS_Cursive("JS Cursive", "JS Cursive.flf"),
    JS_Stick_Letters("JS Stick Letters", "JS Stick Letters.flf"),
    Katakana("Katakana", "Katakana.flf"),
    Kban("Kban", "Kban.flf"),
    Keyboard("Keyboard", "Keyboard.flf"),
    Knob("Knob", "Knob.flf"),
    Konto("Konto", "Konto.flf"),
    Konto_Slant("Konto Slant", "Konto Slant.flf"),
    kontoslant("kontoslant", "kontoslant.flf"),
    larry3d("larry3d", "larry3d.flf"),
    Larry_3D("Larry 3D", "Larry 3D.flf"),
    Larry_3D_2("Larry 3D 2", "Larry 3D 2.flf"),
    LCD("LCD", "LCD.flf"),
    Lean("Lean", "Lean.flf"),
    letter("letter", "letter.tlf"),
    Letters("Letters", "Letters.flf"),
    Lil_Devil("Lil Devil", "Lil Devil.flf"),
    lildevil("lildevil", "lildevil.flf"),
    Line_Blocks("Line Blocks", "Line Blocks.flf"),
    lineblocks("lineblocks", "lineblocks.flf"),
    Linux("Linux", "Linux.flf"),
    Lockergnome("Lockergnome", "Lockergnome.flf"),
    Madrid("Madrid", "Madrid.flf"),
    Marquee("Marquee", "Marquee.flf"),
    Maxfour("Maxfour", "Maxfour.flf"),
    maxiwi("maxiwi", "maxiwi.flf"),
    Merlin1("Merlin1", "Merlin1.flf"),
    Merlin2("Merlin2", "Merlin2.flf"),
    Mike("Mike", "Mike.flf"),
    Mini("Mini", "Mini.flf"),
    miniwi("miniwi", "miniwi.flf"),
    Mirror("Mirror", "Mirror.flf"),
    Mnemonic("Mnemonic", "Mnemonic.flf"),
    Modular("Modular", "Modular.flf"),
    mono9("mono9", "mono9.tlf"),
    mono12("mono12", "mono12.tlf"),
    Morse("Morse", "Morse.flf"),
    Morse2("Morse2", "Morse2.flf"),
    Moscow("Moscow", "Moscow.flf"),
    Mshebrew210("Mshebrew210", "Mshebrew210.flf"),
    Muzzle("Muzzle", "Muzzle.flf"),
    Nancyj("Nancyj", "Nancyj.flf"),
    Nancyj_Fancy("Nancyj-Fancy", "Nancyj-Fancy.flf"),
    Nancyj_Improved("Nancyj-Improved", "Nancyj-Improved.flf"),
    Nancyj_Underlined("Nancyj-Underlined", "Nancyj-Underlined.flf"),
    Nipples("Nipples", "Nipples.flf"),
    NScript("NScript", "NScript.flf"),
    NT_Greek("NT Greek", "NT Greek.flf"),
    ntgreek("ntgreek", "ntgreek.flf"),
    NV_Script("NV Script", "NV Script.flf"),
    O8("O8", "O8.flf"),
    Octal("Octal", "Octal.flf"),
    Ogre("Ogre", "Ogre.flf"),
    Old_Banner("Old Banner", "Old Banner.flf"),
    oldbanner("oldbanner", "oldbanner.flf"),
    OS2("OS2", "OS2.flf"),
    pagga("pagga", "pagga.tlf"),
    Patorjk_s_Cheese("Patorjk's Cheese", "Patorjk's Cheese.flf"),
    Patorjk_HeX("Patorjk-HeX", "Patorjk-HeX.flf"),
    Pawp("Pawp", "Pawp.flf"),
    Peaks("Peaks", "Peaks.flf"),
    Peaks_Slant("Peaks Slant", "Peaks Slant.flf"),
    peaksslant("peaksslant", "peaksslant.flf"),
    Pebbles("Pebbles", "Pebbles.flf"),
    Pepper("Pepper", "Pepper.flf"),
    Poison("Poison", "Poison.flf"),
    Puffy("Puffy", "Puffy.flf"),
    Puzzle("Puzzle", "Puzzle.flf"),
    Pyramid("Pyramid", "Pyramid.flf"),
    Rammstein("Rammstein", "Rammstein.flf"),
    rebel("rebel", "rebel.tlf"),
    Rectangles("Rectangles", "Rectangles.flf"),
    Red_Phoenix("Red Phoenix", "Red Phoenix.flf"),
    red_phoenix("red_phoenix", "red_phoenix.flf"),
    Relief("Relief", "Relief.flf"),
    Relief2("Relief2", "Relief2.flf"),
    rev("rev", "rev.flf"),
    Reverse("Reverse", "Reverse.flf"),
    Roman("Roman", "Roman.flf"),
    Rot13("Rot13", "Rot13.flf"),
    Rotated("Rotated", "Rotated.flf"),
    Rounded("Rounded", "Rounded.flf"),
    Rowan_Cap("Rowan Cap", "Rowan Cap.flf"),
    rowancap("rowancap", "rowancap.flf"),
    Rozzo("Rozzo", "Rozzo.flf"),
    Runic("Runic", "Runic.flf"),
    Runyc("Runyc", "Runyc.flf"),
    rusto("rusto", "rusto.tlf"),
    rustofat("rustofat", "rustofat.tlf"),
    S_Blood("S Blood", "S Blood.flf"),
    s_relief("s-relief", "s-relief.flf"),
    Santa_Clara("Santa Clara", "Santa Clara.flf"),
    santaclara("santaclara", "santaclara.flf"),
    sblood("sblood", "sblood.flf"),
    Script("Script", "Script.flf"),
    Serifcap("Serifcap", "Serifcap.flf"),
    Shadow("Shadow", "Shadow.flf"),
    Shimrod("Shimrod", "Shimrod.flf"),
    Short("Short", "Short.flf"),
    SL_Script("SL Script", "SL Script.flf"),
    Slant("Slant", "Slant.flf"),
    Slant_Relief("Slant Relief", "Slant Relief.flf"),
    Slide("Slide", "Slide.flf"),
    slscript("slscript", "slscript.flf"),
    Small("Small", "Small.flf"),
    Small_Caps("Small Caps", "Small Caps.flf"),
    Small_Isometric1("Small Isometric1", "Small Isometric1.flf"),
    Small_Keyboard("Small Keyboard", "Small Keyboard.flf"),
    Small_Poison("Small Poison", "Small Poison.flf"),
    Small_Script("Small Script", "Small Script.flf"),
    Small_Shadow("Small Shadow", "Small Shadow.flf"),
    Small_Slant("Small Slant", "Small Slant.flf"),
    Small_Tengwar("Small Tengwar", "Small Tengwar.flf"),
    smallcaps("smallcaps", "smallcaps.flf"),
    smascii9("smascii9", "smascii9.tlf"),
    smascii12("smascii12", "smascii12.tlf"),
    smblock("smblock", "smblock.tlf"),
    smbraille("smbraille", "smbraille.tlf"),
    smisome1("smisome1", "smisome1.flf"),
    smkeyboard("smkeyboard", "smkeyboard.flf"),
    smmono9("smmono9", "smmono9.tlf"),
    smmono12("smmono12", "smmono12.tlf"),
    smpoison("smpoison", "smpoison.flf"),
    smscript("smscript", "smscript.flf"),
    smshadow("smshadow", "smshadow.flf"),
    smslant("smslant", "smslant.flf"),
    smtengwar("smtengwar", "smtengwar.flf"),
    Soft("Soft", "Soft.flf"),
    Speed("Speed", "Speed.flf"),
    Spliff("Spliff", "Spliff.flf"),
    Stacey("Stacey", "Stacey.flf"),
    Stampate("Stampate", "Stampate.flf"),
    Stampatello("Stampatello", "Stampatello.flf"),
    Standard("Standard", "Standard.flf"),
    Star_Strips("Star Strips", "Star Strips.flf"),
    Star_Wars("Star Wars", "Star Wars.flf"),
    starstrips("starstrips", "starstrips.flf"),
    starwars("starwars", "starwars.flf"),
    Stellar("Stellar", "Stellar.flf"),
    Stforek("Stforek", "Stforek.flf"),
    Stick_Letters("Stick Letters", "Stick Letters.flf"),
    Stop("Stop", "Stop.flf"),
    Straight("Straight", "Straight.flf"),
    Stronger_Than_All("Stronger Than All", "Stronger Than All.flf"),
    Sub_Zero("Sub-Zero", "Sub-Zero.flf"),
    Swamp_Land("Swamp Land", "Swamp Land.flf"),
    swampland("swampland", "swampland.flf"),
    Swan("Swan", "Swan.flf"),
    Sweet("Sweet", "Sweet.flf"),
    Tanja("Tanja", "Tanja.flf"),
    Tengwar("Tengwar", "Tengwar.flf"),
    Term("Term", "Term.flf"),
    Test1("Test1", "Test1.flf"),
    The_Edge("The Edge", "The Edge.flf"),
    Thick("Thick", "Thick.flf"),
    Thin("Thin", "Thin.flf"),
    THIS("THIS", "THIS.flf"),
    Thorned("Thorned", "Thorned.flf"),
    Three_Point("Three Point", "Three Point.flf"),
    threepoint("threepoint", "threepoint.flf"),
    Ticks("Ticks", "Ticks.flf"),
    Ticks_Slant("Ticks Slant", "Ticks Slant.flf"),
    ticksslant("ticksslant", "ticksslant.flf"),
    Tiles("Tiles", "Tiles.flf"),
    Tinker_Toy("Tinker-Toy", "Tinker-Toy.flf"),
    Tombstone("Tombstone", "Tombstone.flf"),
    Train("Train", "Train.flf"),
    Trek("Trek", "Trek.flf"),
    Tsalagi("Tsalagi", "Tsalagi.flf"),
    Tubular("Tubular", "Tubular.flf"),
    Twisted("Twisted", "Twisted.flf"),
    Two_Point("Two Point", "Two Point.flf"),
    twopoint("twopoint", "twopoint.flf"),
    Univers("Univers", "Univers.flf"),
    USA_Flag("USA Flag", "USA Flag.flf"),
    usaflag("usaflag", "usaflag.flf"),
    Varsity("Varsity", "Varsity.flf"),
    Wavy("Wavy", "Wavy.flf"),
    Weird("Weird", "Weird.flf"),
    Wet_Letter("Wet Letter", "Wet Letter.flf"),
    wetletter("wetletter", "wetletter.flf"),
    Whimsy("Whimsy", "Whimsy.flf"),
    wideterm("wideterm", "wideterm.tlf"),
    Wow("Wow", "Wow.flf"),
    ;

    private final String name;
    private final String filename;

    XeroFonts(String name, String filename) {
        this.name = name;
        this.filename = filename;
    }

    public static Figlet.FontSpec random() {
        var values = values();
        return values[new Random().nextInt(values.length)];
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getFilename() {
        return ("banana/fonts/") + filename;
    }
}
