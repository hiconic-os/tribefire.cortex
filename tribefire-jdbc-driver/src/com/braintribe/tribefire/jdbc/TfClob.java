// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.tribefire.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import org.apache.commons.io.output.StringBuilderWriter;

import com.braintribe.logging.Logger;
import com.braintribe.utils.IOTools;

/**
 * The Class TfClob.
 *
 */
public class TfClob implements Clob {

	protected static Logger logger = Logger.getLogger(TfClob.class);
	protected StringBuilder sb = new StringBuilder();
	
	/* (non-Javadoc)
	 * @see java.sql.Clob#length()
	 */
	@Override
	public long length() throws SQLException {
		return sb.length();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#getSubString(long, int)
	 */
	@Override
	public String getSubString(long pos, int length) throws SQLException {
		return sb.substring((int) pos, (int) pos+length);
	}

	@Override
	public Reader getCharacterStream() throws SQLException {
		return new StringReader(sb.toString());
	}

	@Override
	public InputStream getAsciiStream() throws SQLException {
		try {
			return new ByteArrayInputStream(sb.toString().getBytes("ASCII"));
		} catch (Exception e) {
			throw new SQLException("Could not get ASCII stream.", e);
		}
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#position(java.lang.String, long)
	 */
	@Override
	public long position(String searchstr, long start) throws SQLException {
		return sb.indexOf(searchstr, (int) start); 
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#position(java.sql.Clob, long)
	 */
	@Override
	public long position(Clob searchstr, long start) throws SQLException {
		String searchString = null;
		Reader r = null;
		try {
			r = searchstr.getCharacterStream();
			searchString = IOTools.slurp(r);
			r.close();
		} catch(Exception e) {
			throw new SQLException("Could not read content from searchstr "+searchstr, e);
		} finally {
			IOTools.closeCloseable(r, logger);
		}

		return position(searchString, start);
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setString(long, java.lang.String)
	 */
	@Override
	public int setString(long pos, String str) throws SQLException {
		sb.insert((int) pos, str); 
		return str.length();
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setString(long, java.lang.String, int, int)
	 */
	@Override
	public int setString(long pos, String str, int offset, int len) throws SQLException {
		sb.insert((int) pos, str.toCharArray(), offset, len); 
		return len;
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setAsciiStream(long)
	 */
	@Override
	public OutputStream setAsciiStream(long pos) throws SQLException {
		throw new SQLFeatureNotSupportedException(TfMetadata.getUnsupportedMessage());
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#setCharacterStream(long)
	 */
	@Override
	public Writer setCharacterStream(long pos) throws SQLException {
		sb.setLength((int) pos);
		StringBuilderWriter sbw = new StringBuilderWriter(this.sb);
		return sbw;
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#truncate(long)
	 */
	@Override
	public void truncate(long len) throws SQLException {
		sb.setLength((int) len);
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#free()
	 */
	@Override
	public void free() throws SQLException {
		//Nothing to do
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#getCharacterStream(long, long)
	 */
	@Override
	public Reader getCharacterStream(long pos, long length) throws SQLException {
		StringReader sr = new StringReader(sb.substring((int) pos, (int) (pos+length)));
		return sr;
	}

}
