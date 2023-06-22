import styles from './styles/noresultblock.module.css'

function NoResultBlock({searchQuery}) {

    return(
        <div className={styles.NoResultBlock}>
        <p>
          Your search - <strong>{searchQuery}</strong> - did not match any documents.
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