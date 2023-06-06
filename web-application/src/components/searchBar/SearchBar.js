import './styles/searchbar.css'
import { useNavigate } from "react-router-dom";

function SearchBar({ searchQuery, handleSearchInputChange }) {
    const navigate = useNavigate();

    const handleSubmit = () => {
        if (searchQuery.trim() !== '') {
            navigate('/searchResult');
        }
    };


    return (
        <div>
            <div class="input-group">
                <div class="form-outline">
                    <input
                        id="search-focus"
                        type="search"
                        class="form-control"
                        onChange={handleSearchInputChange}
                        placeholder="Enter your search query"
                        style={{
                            width: '30rem',
                            borderTopRightRadius: '0',
                            borderBottomRightRadius: '0',
                            borderTopLeftRadius: '2rem',
                            borderBottomLeftRadius: '2rem',
                            boxShadow: 'none'
                        }}
                        value={searchQuery}
                    />
                </div>
                <button
                    onClick={handleSubmit}
                    type="button"
                    class="btn btn-primary"
                    style={{
                        width: '5rem',
                        borderTopRightRadius: '2rem',
                        borderBottomRightRadius: '2rem',
                        borderTopLeftRadius: '0',
                        borderBottomLeftRadius: '0',
                    }}
                >
                    <i class="fas fa-search"></i>
                </button>
            </div>
        </div>
    )
}

export default SearchBar