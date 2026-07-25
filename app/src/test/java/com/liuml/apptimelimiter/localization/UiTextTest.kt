package com.liuml.apptimelimiter.localization

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class UiTextTest {
    @Test
    fun `chinese mode preserves source text`() {
        assertEquals(
            "退出前提醒",
            UiText.translate("退出前提醒", SupportedLanguage.CHINESE),
        )
    }

    @Test
    fun `english mode translates static setting labels`() {
        assertEquals(
            "Long vibration",
            UiText.translate("长震动提醒", SupportedLanguage.ENGLISH),
        )
        assertEquals(
            "System default",
            UiText.translate("跟随系统", SupportedLanguage.ENGLISH),
        )
    }

    @Test
    fun `english mode translates structured dynamic labels`() {
        assertEquals(
            "3 apps enabled",
            UiText.translate("已启用 3 个应用", SupportedLanguage.ENGLISH),
        )
        assertEquals(
            "2 launches · 1 limit hits",
            UiText.translate("启动 2 次 · 限制触发 1 次", SupportedLanguage.ENGLISH),
        )
    }

    @Test
    fun `unknown app and diagnostic content remains unchanged`() {
        assertEquals(
            "com.example.custom",
            UiText.translate("com.example.custom", SupportedLanguage.ENGLISH),
        )
    }

    @Test
    fun `group member summary translates separators and overflow suffix`() {
        assertEquals(
            "6 apps: App A, App B, etc.",
            UiText.translate("6 个应用：App A、App B 等", SupportedLanguage.ENGLISH),
        )
    }

    @Test
    fun `recent feature settings and notice text have english coverage`() {
        val recentUiText = listOf(
            "分组额度与规则",
            "固定管控规则",
            "提醒与延时",
            "统计与诊断",
            "应用设置",
            "维护与支持",
            "精细管控",
            "把使用边界设清楚",
            "• 任一规则先到即执行退出",
            "本次计划",
            "欢迎使用时停",
            "加入内测",
            "支持时停开发",
            "支付宝",
            "微信支付",
            "查看软件声明",
            "软件声明",
            "Hook 计数来自旧版本，请强停并重新打开应用",
        )

        recentUiText.forEach { source ->
            val translated = UiText.translate(source, SupportedLanguage.ENGLISH)
            assertFalse("Untranslated English UI text: $source", CJK.containsMatchIn(translated))
        }
    }

    @Test
    fun `recent dynamic labels have english coverage`() {
        val dynamicUiText = listOf(
            "2 个应用分组",
            "共享每日：已用 5分 / 10分 · 剩余 5分",
            "Example（已有个人设置）",
            "Example（个人设置已暂停）",
            "加入 QQ 群：1009712674",
        )

        dynamicUiText.forEach { source ->
            val translated = UiText.translate(source, SupportedLanguage.ENGLISH)
            assertFalse("Untranslated English UI text: $source", CJK.containsMatchIn(translated))
        }
    }

    private companion object {
        val CJK = Regex("[\\u4e00-\\u9fff]")
    }
}
