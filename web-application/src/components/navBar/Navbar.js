import styles from './styles/navbar.module.css'
import Title from '../common/Title';
import SearchBar from '../searchBar/SearchBar';

function Navbar({ searchQuery, handleSearchInputChange, handleSearchResults }) {

    return (
        <div className={styles.Navbar}>
            <div className={styles.NavbarSearchFrame}>
                <Title fontSize={2} />
                <SearchBar searchQuery={searchQuery} handleSearchInputChange={handleSearchInputChange} handleSearchResults={handleSearchResults} />
            </div>
        </div>
    )
}

export default Navbar;