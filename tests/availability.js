process.env.NODE_ENV = 'test'

let chai = require('chai')
let chaiHttp = require('chai-http')
let server = require('../server.js')
let should = chai.should()


chai.use(chaiHttp) // enable HTTP

describe('/ ', () => {
    it('should return status 200 ok', (done) => {
        chai.request(server)
            .get('/')
            .end((err, res) => {
                res.should.have.status(200)
                res.should.be.json
                res.should.be.a('object')
                res.body.should.have.all.keys(
                    'reachableFromUrl', 'stopUrl', 'departuresUrl', 'arrivalsUrl', 'journeysUrl',
                    'tripUrl', 'nearbyUrl', 'locationsUrl', 'radarUrl', 'refreshJourneyUrl')
                done()
            });
    });
});

describe('/locations/nearby', () => {
    it('should return status 200 ok and 6 results', (done) => {
        chai.request(server)
            .get('/locations/nearby/?latitude=47.06727184602459&longitude=15.442097181893473&distance=250')
            .end((err, res) => {
                res.should.have.status(200)
                res.should.be.json
                res.body.should.be.a('array')
                res.body.should.have.lengthOf(5)
                res.body[0].should.be.a('object')
                done()
            });
    });
});