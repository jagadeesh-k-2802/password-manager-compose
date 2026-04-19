package com.jackappsdev.password_manager.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordItemModelTest {

    private fun makeModel(
        id: Int? = 1,
        name: String = "Google",
        username: String = "user@google.com",
        password: String = "pass123",
        notes: String = "Main account",
        website: String = "https://google.com",
        isAddedToWatch: Boolean = false,
        categoryId: Int? = null,
        images: List<ByteArray> = emptyList(),
        createdAt: Long? = 1_000_000L
    ) = PasswordItemModel(
        id, name, username, password, notes, website, isAddedToWatch, categoryId, images, createdAt
    )

    // region equals

    @Test
    fun `equals returns true for models with same scalar fields and no images`() {
        val a = makeModel()
        val b = makeModel()
        assertEquals(a, b)
    }

    @Test
    fun `equals returns false when names differ`() {
        assertNotEquals(makeModel(name = "Google"), makeModel(name = "Yahoo"))
    }

    @Test
    fun `equals returns false when passwords differ`() {
        assertNotEquals(makeModel(password = "pass1"), makeModel(password = "pass2"))
    }

    @Test
    fun `equals returns false when isAddedToWatch differs`() {
        assertNotEquals(makeModel(isAddedToWatch = false), makeModel(isAddedToWatch = true))
    }

    @Test
    fun `equals returns false when categoryId differs`() {
        assertNotEquals(makeModel(categoryId = null), makeModel(categoryId = 5))
    }

    @Test
    fun `equals returns true when both models have identical images`() {
        val img = byteArrayOf(1, 2, 3)
        val a = makeModel(images = listOf(img.copyOf()))
        val b = makeModel(images = listOf(img.copyOf()))
        assertEquals(a, b)
    }

    @Test
    fun `equals returns false when images differ in content`() {
        val a = makeModel(images = listOf(byteArrayOf(1, 2, 3)))
        val b = makeModel(images = listOf(byteArrayOf(4, 5, 6)))
        assertNotEquals(a, b)
    }

    @Test
    fun `equals returns false when image count differs`() {
        val a = makeModel(images = listOf(byteArrayOf(1)))
        val b = makeModel(images = listOf(byteArrayOf(1), byteArrayOf(2)))
        assertNotEquals(a, b)
    }

    @Test
    fun `equals returns false when compared to null`() {
        val model = makeModel()
        assertFalse(model.equals(null))
    }

    @Test
    fun `equals returns false when compared to different type`() {
        val model = makeModel()
        assertFalse(model.equals("not a model"))
    }

    // endregion

    // region hashCode

    @Test
    fun `hashCode is consistent across multiple calls`() {
        val model = makeModel()
        assertEquals(model.hashCode(), model.hashCode())
    }

    @Test
    fun `equal models have equal hashCodes`() {
        val a = makeModel()
        val b = makeModel()
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `models with different ids have different hashCodes`() {
        val a = makeModel(id = 1)
        val b = makeModel(id = 2)
        assertNotEquals(a.hashCode(), b.hashCode())
    }

    // endregion

    // region default values

    @Test
    fun `default images list is empty`() {
        val model = PasswordItemModel(
            id = null, name = "Test", username = "u", password = "p",
            notes = "", website = "", isAddedToWatch = false
        )
        assertTrue(model.images.isEmpty())
    }

    @Test
    fun `default categoryId is null`() {
        val model = PasswordItemModel(
            id = null, name = "Test", username = "u", password = "p",
            notes = "", website = "", isAddedToWatch = false
        )
        assertTrue(model.categoryId == null)
    }

    // endregion
}
