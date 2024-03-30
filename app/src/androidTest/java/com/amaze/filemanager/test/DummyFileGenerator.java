/*
 * Copyright (C) 2014-2024 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.amaze.filemanager.test;

import static com.amaze.filemanager.filesystem.files.GenericCopyUtil.DEFAULT_BUFFER_SIZE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;

/**
 * Generate file of specified size using randomly generated bytes.
 *
 * <p>Because of the need that both Espresso and Robolectric tests depends on it, it needs to be
 * placed at the main source tree, and hide it using {@link VisibleForTesting}.
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
@RestrictTo(RestrictTo.Scope.TESTS)
public abstract class DummyFileGenerator {

  /**
   * @param destFile File to be generated with random bytes
   * @param size file size
   * @return SHA1 checksum of the generated file, as byte array
   * @throws IOException in case any I/O error occurred
   */
  @VisibleForTesting(otherwise = VisibleForTesting.NONE)
  @RestrictTo(RestrictTo.Scope.TESTS)
  public static byte[] createFile(@NonNull File destFile, int size) throws IOException {
    Random rand = new SecureRandom();
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException shouldNeverHappen) {
      throw new IOException("SHA-1 implementation not found");
    }

    FileOutputStream out = new FileOutputStream(destFile);
    DigestOutputStream dout = new DigestOutputStream(out, md);
    int count = 0;
    for (int i = size; i >= 0; i -= DEFAULT_BUFFER_SIZE, count += DEFAULT_BUFFER_SIZE) {
      byte[] bytes = new byte[i > DEFAULT_BUFFER_SIZE ? DEFAULT_BUFFER_SIZE : i];
      rand.nextBytes(bytes);
      dout.write(bytes);
    }
    dout.flush();
    dout.close();

    return md.digest();
  }
}
