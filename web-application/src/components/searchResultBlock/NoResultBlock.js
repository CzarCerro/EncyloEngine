import React, { useState } from 'react';
import styles from './styles/noresultblock.module.css'

function NoResultBlock({searchQuery}) {
  const notFoundQuery = useState(searchQuery)[0];

    return(
        <div className={styles.NoResultBlock}>
        <p>
          Your search - <strong>{notFoundQuery}</strong> - did not match any documents.
        </p>
        <p>
          Suggestions:
        </p>
        <ul>
          <li>Make sure that all words are spelled correctly.</li>
          <li>Try different keywords.</li>
          <li>Try more general keywords.</li>
        </ul>
      </div>
    );
}

export default NoResultBlock;