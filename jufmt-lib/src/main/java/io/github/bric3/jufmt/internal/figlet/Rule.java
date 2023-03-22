package io.github.bric3.jufmt.internal.figlet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Yihleego
 */
class Rule {
    private Layout horizontalLayout;
    private boolean horizontal1;
    private boolean horizontal2;
    private boolean horizontal3;
    private boolean horizontal4;
    private boolean horizontal5;
    private boolean horizontal6;
    private Layout verticalLayout;
    private boolean vertical1;
    private boolean vertical2;
    private boolean vertical3;
    private boolean vertical4;
    private boolean vertical5;

    public Rule() {
    }

    private Rule(
            @Nullable Layout horizontalLayout,
            boolean horizontal1,
            boolean horizontal2,
            boolean horizontal3,
            boolean horizontal4,
            boolean horizontal5,
            boolean horizontal6,
            @Nullable Layout verticalLayout,
            boolean vertical1,
            boolean vertical2,
            boolean vertical3,
            boolean vertical4,
            boolean vertical5
    ) {
        this.horizontalLayout = horizontalLayout;
        this.horizontal1 = horizontal1;
        this.horizontal2 = horizontal2;
        this.horizontal3 = horizontal3;
        this.horizontal4 = horizontal4;
        this.horizontal5 = horizontal5;
        this.horizontal6 = horizontal6;
        this.verticalLayout = verticalLayout;
        this.vertical1 = vertical1;
        this.vertical2 = vertical2;
        this.vertical3 = vertical3;
        this.vertical4 = vertical4;
        this.vertical5 = vertical5;
    }

    static @NotNull Rule getSmushRule(int oldLayout, @Nullable Integer newLayout) {
        var rules = new HashMap<String, Integer>();
        int layout = newLayout != null ? newLayout : oldLayout;
        for (var rule : RuleEnum.values()) {
            var key = rule.getKey();
            int code = rule.getCode();
            int value = rule.getValue();
            if (layout >= code) {
                layout -= code;
                if (!rules.containsKey(key)) {
                    rules.put(key, value);
                }
            } else {
                if (!Objects.equals(RuleEnum.HORIZONTAL_LAYOUT_SMUSH.getKey(), key)) {
                    if (!Objects.equals(RuleEnum.VERTICAL_LAYOUT_SMUSH.getKey(), key)) {
                        rules.put(key, 0);
                    }
                }
            }
        }
        var a = rules.get(RuleEnum.VERTICAL_5.getKey());
        var a1 = rules.get(RuleEnum.VERTICAL_4.getKey());
        var a2 = rules.get(RuleEnum.VERTICAL_3.getKey());
        var a3 = rules.get(RuleEnum.VERTICAL_2.getKey());
        var a4 = rules.get(RuleEnum.VERTICAL_1.getKey());
        var a5 = rules.get(RuleEnum.HORIZONTAL_6.getKey());
        var a6 = rules.get(RuleEnum.HORIZONTAL_5.getKey());
        var a7 = rules.get(RuleEnum.HORIZONTAL_4.getKey());
        var a8 = rules.get(RuleEnum.HORIZONTAL_3.getKey());
        var a9 = rules.get(RuleEnum.HORIZONTAL_2.getKey());
        var a10 = rules.get(RuleEnum.HORIZONTAL_1.getKey());
        var rule = new Rule(
                Layout.get(rules.get(RuleEnum.HORIZONTAL_LAYOUT_SMUSH.getKey())),
                Objects.equals(a10, 1),
                Objects.equals(a9, 1),
                Objects.equals(a8, 1),
                Objects.equals(a7, 1),
                Objects.equals(a6, 1),
                Objects.equals(a5, 1),
                Layout.get(rules.get(RuleEnum.VERTICAL_LAYOUT_SMUSH.getKey())),
                Objects.equals(a4, 1),
                Objects.equals(a3, 1),
                Objects.equals(a2, 1),
                Objects.equals(a1, 1),
                Objects.equals(a, 1)
        );
        if (rule.getHorizontalLayout() == null) {
            if (oldLayout == 0) {
                rule.horizontalLayout = Layout.FITTED;
            } else if (oldLayout == -1) {
                rule.horizontalLayout = Layout.FULL;
            } else if (rule.isHorizontal1()
                    || rule.isHorizontal2()
                    || rule.isHorizontal3()
                    || rule.isHorizontal4()
                    || rule.isHorizontal5()
                    || rule.isHorizontal6()) {
                rule.horizontalLayout = Layout.SMUSH_R;
            } else {
                rule.horizontalLayout = Layout.SMUSH_U;
            }
        } else if (rule.getHorizontalLayout() == Layout.SMUSH_U
                && rule.isHorizontal1()
                || rule.isHorizontal2()
                || rule.isHorizontal3()
                || rule.isHorizontal4()
                || rule.isHorizontal5()
                || rule.isHorizontal6()) {
            rule.horizontalLayout = Layout.SMUSH_R;
        }
        if (rule.getVerticalLayout() == null) {
            if (rule.isVertical1()
                    || rule.isVertical2()
                    || rule.isVertical3()
                    || rule.isVertical4()
                    || rule.isVertical5()) {
                rule.verticalLayout = Layout.SMUSH_R;
            } else {
                rule.verticalLayout = Layout.FULL;
            }
        } else if (rule.getVerticalLayout() == Layout.SMUSH_U
                && rule.isVertical1()
                || rule.isVertical2()
                || rule.isVertical3()
                || rule.isVertical4()
                || rule.isVertical5()) {
            rule.verticalLayout = Layout.SMUSH_R;
        }
        return rule;
    }

    public @NotNull Rule copy() {
        return new Rule(
                horizontalLayout, horizontal1, horizontal2, horizontal3, horizontal4, horizontal5, horizontal6,
                verticalLayout, vertical1, vertical2, vertical3, vertical4, vertical5
        );
    }

    private void configureHorizontal(@NotNull Layout horizontalLayout, boolean horizontal1, boolean horizontal2, boolean horizontal3, boolean horizontal4, boolean horizontal5, boolean horizontal6) {
        this.horizontalLayout = horizontalLayout;
        this.horizontal1 = horizontal1;
        this.horizontal2 = horizontal2;
        this.horizontal3 = horizontal3;
        this.horizontal4 = horizontal4;
        this.horizontal5 = horizontal5;
        this.horizontal6 = horizontal6;
    }

    Rule withHorizontalLayout(@Nullable Layout layout) {
        if (layout == null || layout == Layout.DEFAULT) {
            return this;
        }
        switch (layout) {
            case FULL:
                configureHorizontal(Layout.FULL, false, false, false, false, false, false);
                break;
            case FITTED:
                configureHorizontal(Layout.FITTED, false, false, false, false, false, false);
                break;
            case SMUSH_U:
                configureHorizontal(Layout.SMUSH_U, false, false, false, false, false, false);
                break;
            case SMUSH_R:
                configureHorizontal(Layout.SMUSH_R, true, true, true, true, true, true);
                break;
        }
        return this;
    }

    private void configureVertical(@NotNull Layout verticalLayout, boolean vertical1, boolean vertical2, boolean vertical3, boolean vertical4, boolean vertical5) {
        this.verticalLayout = verticalLayout;
        this.vertical1 = vertical1;
        this.vertical2 = vertical2;
        this.vertical3 = vertical3;
        this.vertical4 = vertical4;
        this.vertical5 = vertical5;
    }

    Rule withVerticalLayout(@Nullable Layout layout) {
        if (layout == null || layout == Layout.DEFAULT) {
            return this;
        }
        switch (layout) {
            case FULL:
                configureVertical(Layout.FULL, false, false, false, false, false);
                break;
            case FITTED:
                configureVertical(Layout.FITTED, false, false, false, false, false);
                break;
            case SMUSH_U:
                configureVertical(Layout.SMUSH_U, false, false, false, false, false);
                break;
            case SMUSH_R:
                configureVertical(Layout.SMUSH_R, true, true, true, true, true);
                break;
        }
        return this;
    }

    public Layout getHorizontalLayout() {
        return horizontalLayout;
    }

    public boolean isHorizontal1() {
        return horizontal1;
    }


    public boolean isHorizontal2() {
        return horizontal2;
    }


    public boolean isHorizontal3() {
        return horizontal3;
    }


    public boolean isHorizontal4() {
        return horizontal4;
    }


    public boolean isHorizontal5() {
        return horizontal5;
    }


    public boolean isHorizontal6() {
        return horizontal6;
    }


    public Layout getVerticalLayout() {
        return verticalLayout;
    }

    public boolean isVertical1() {
        return vertical1;
    }


    public boolean isVertical2() {
        return vertical2;
    }


    public boolean isVertical3() {
        return vertical3;
    }


    public boolean isVertical4() {
        return vertical4;
    }


    public boolean isVertical5() {
        return vertical5;
    }

}
