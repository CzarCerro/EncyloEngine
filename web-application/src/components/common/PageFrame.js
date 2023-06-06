import React from 'react';
import styles from './styles/pageframe.module.css';

function PageFrame({ children }) {
  return (
    <div className={styles.PageFrame}>
      {children}
    </div>
  );
}

export default PageFrame;
