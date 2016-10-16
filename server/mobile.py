from flask import Flask, jsonify, g, request
import os
import sqlite3
import time

app = Flask(__name__)

work_dir = os.path.dirname(os.path.realpath(__file__))
data_file = '{}/webres/data.db'.format(work_dir)


def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(data_file)
    return db


@app.route('/mobile')
def mobile():

    data_from = request.args.get('data_from', 0, type=int)
    time_now = int(round(time.time(), 0))

    db = get_db()

    with db:
        cursor = db.cursor()
        results = cursor.execute(
            'SELECT spawnid, latitude, longitude, spawntype, pokeid, expiretime FROM spawns WHERE (expiretime > ?) AND (fromtime >= ?)',
            [time_now, data_from])

        pokemons = []

        for result in results.fetchall():
            pokemon = {'spawn_id': result[0], 'latitude': result[1], 'longitude': result[2],
                       'spawn_type': result[3], 'pokemon_id': result[4], 'expire_time': result[5]}
            pokemons.append(pokemon)

    return jsonify({'time': time_now, 'data': pokemons})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, threaded=True)
