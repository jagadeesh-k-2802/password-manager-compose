package com.jackappsdev.password_manager.shared.core

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VersionTrackerTest {

    @Test
    fun `isVersionAtLeast returns false when version is null`() {
        assertFalse(VersionTracker.isVersionAtLeast(null, "1.7.0"))
    }

    @Test
    fun `isVersionAtLeast returns false when version is older`() {
        assertFalse(VersionTracker.isVersionAtLeast("1.6.0", "1.7.0"))
        assertFalse(VersionTracker.isVersionAtLeast("1.6.9", "1.7.0"))
        assertFalse(VersionTracker.isVersionAtLeast("0.9.0", "1.7.0"))
    }

    @Test
    fun `isVersionAtLeast returns true when version is exactly minVersion`() {
        assertTrue(VersionTracker.isVersionAtLeast("1.7.0", "1.7.0"))
    }

    @Test
    fun `isVersionAtLeast returns true when version is newer`() {
        assertTrue(VersionTracker.isVersionAtLeast("1.7.1", "1.7.0"))
        assertTrue(VersionTracker.isVersionAtLeast("1.8.0", "1.7.0"))
        assertTrue(VersionTracker.isVersionAtLeast("2.0.0", "1.7.0"))
    }

    @Test
    fun `isVersionAtLeast works with different minVersions`() {
        assertTrue(VersionTracker.isVersionAtLeast("2.5.3", "2.5.0"))
        assertFalse(VersionTracker.isVersionAtLeast("2.4.9", "2.5.0"))
    }
}
