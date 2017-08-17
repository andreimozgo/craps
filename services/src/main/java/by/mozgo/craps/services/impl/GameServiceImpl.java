package by.mozgo.craps.services.impl;

import by.mozgo.craps.dao.DaoException;
import by.mozgo.craps.dao.GameDao;
import by.mozgo.craps.dao.impl.GameDaoImpl;
import by.mozgo.craps.entity.Game;
import by.mozgo.craps.services.GameService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Andrei Mozgo. 2017.
 */
public class GameServiceImpl extends ServiceImpl<Game> implements GameService {
    private static final Logger LOG = LogManager.getLogger();
    private static GameServiceImpl instance = null;
    private GameDao gameDao = GameDaoImpl.getInstance();

    private GameServiceImpl() {
        baseDao = gameDao;
    }

    public static GameServiceImpl getInstance() {
        if (instance == null) {
            instance = new GameServiceImpl();
        }
        return instance;
    }

    @Override
    public Game findEntityById(Integer id) {
        return null;
    }

    @Override
    public void delete(Integer id) {
    }

    @Override
    public void update(Game game) {
    }

    @Override
    public int findGamesNumber(int userId){
        int number = 0;
        try {
            number = gameDao.findGamesNumber(userId);
        } catch (DaoException e) {
            LOG.log(Level.ERROR, "Exception in DAO {}", e);
        }
        return number;
    }
}
