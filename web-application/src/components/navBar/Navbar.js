import styles from './styles/navbar.module.css'
import Title from '../common/Title';
import SearchBar from '../searchBar/SearchBar';

function Navbar({ searchQuery, handleSearchInputChange }) {

    return (
        <div className={styles.Navbar}>
            <div className={styles.NavbarSearchFrame}>
                <Title fontSize={2.5} />
                <SearchBar searchQuery={searchQuery} handleSearchInputChange={handleSearchInputChange} />
            </div>
        </div>
    )
}

export default Navbar;