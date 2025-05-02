const pool = require('./db');

// User operations
const createUser = async (username, password, email) => {
  const [result] = await pool.execute(
    'INSERT INTO users (username, password, email) VALUES (?, ?, ?)',
    [username, password, email]
  );
  return result.insertId;
};

const getUserById = async (userId) => {
  const [rows] = await pool.execute('SELECT * FROM users WHERE id = ?', [userId]);
  return rows[0];
};

// File operations
const createFile = async (userId, filename, filepath, size) => {
  const [result] = await pool.execute(
    'INSERT INTO files (user_id, filename, filepath, size) VALUES (?, ?, ?, ?)',
    [userId, filename, filepath, size]
  );
  return result.insertId;
};

const getFileById = async (fileId) => {
  const [rows] = await pool.execute('SELECT * FROM files WHERE id = ?', [fileId]);
  return rows[0];
};

// Shared file operations
const shareFile = async (fileId, sharedWithUserId, permissionLevel = 'view') => {
  const [result] = await pool.execute(
    'INSERT INTO shared_files (file_id, shared_with_user_id, permission_level) VALUES (?, ?, ?)',
    [fileId, sharedWithUserId, permissionLevel]
  );
  return result.insertId;
};

const getSharedFilesForUser = async (userId) => {
  const [rows] = await pool.execute(
    'SELECT * FROM shared_files WHERE shared_with_user_id = ?',
    [userId]
  );
  return rows;
};

module.exports = {
  createUser,
  getUserById,
  createFile,
  getFileById,
  shareFile,
  getSharedFilesForUser
};