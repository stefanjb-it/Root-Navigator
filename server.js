const createHafas = require('hafas-client')
const oebbProfile = require('hafas-client/p/oebb')
const createApi = require('hafas-rest-api')

const config = {
	hostname: 'STVL2',
	name: 'elcapo-paris',
	homepage: 'https://github.com/stefanjb-it/elcapo-paris',
	version: '1.0.0',
	aboutPage: false
}

const hafas = createHafas(oebbProfile, 'elcapo-paris')

const api = createApi(hafas, config)

api.listen(8080, (err) => {
	if (err) console.error(err)
})

module.exports = api;