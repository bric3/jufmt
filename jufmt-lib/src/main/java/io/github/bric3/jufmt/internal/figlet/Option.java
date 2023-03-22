package io.github.bric3.jufmt.internal.figlet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Yihleego
 */
class Option {
    public final int baseline;
    @Nullable
    public final Integer codeTagCount;
    @NotNull
    public final Rule rule;
    @Nullable
    public final Integer fullLayout;
    @NotNull
    public final String hardBlank;
    public final int height;
    public final int maxLength;
    public final int numCommentLines;
    public final int oldLayout;
    public final int printDirection;

    public Option(int baseline,
                  @Nullable Integer codeTagCount,
                  @NotNull Rule rule,
                  @Nullable Integer fullLayout,
                  @NotNull String hardBlank,
                  int height,
                  int maxLength,
                  int numCommentLines,
                  int oldLayout,
                  int printDirection
    ) {
        this.baseline = baseline;
        this.codeTagCount = codeTagCount;
        this.rule = rule;
        this.fullLayout = fullLayout;
        this.hardBlank = hardBlank;
        this.height = height;
        this.maxLength = maxLength;
        this.numCommentLines = numCommentLines;
        this.oldLayout = oldLayout;
        this.printDirection = printDirection;
    }

    public Option copy() {
        return new Option(
                baseline,
                codeTagCount,
                rule.copy(),
                fullLayout,
                hardBlank,
                height,
                maxLength,
                numCommentLines,
                oldLayout,
                printDirection
        );
    }

    public Option withLayout(
            @Nullable Layout horizontalLayout,
            @Nullable Layout verticalLayout
    ) {
        if ((horizontalLayout == null || horizontalLayout == Layout.DEFAULT)
                && (verticalLayout == null || verticalLayout == Layout.DEFAULT)) {
            return this;
        }
        var newOption = copy();
        newOption.rule
                .withHorizontalLayout(horizontalLayout)
                .withVerticalLayout(verticalLayout);
        return newOption;
    }


}

