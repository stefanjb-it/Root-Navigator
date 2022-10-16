process.env.NODE_ENV = 'test'

let chai = require('chai')
let chaiHttp = require('chai-http')
let server = require('../server.js')
let should = chai.should()


chai.use(chaiHttp)

describe('/GET ', () => {
    it('it should GET status 200 ok back', (done) => {
        chai.request(server)
            .get('/')
            .end((err, res) => {
                res.should.have.status(200)
                res.should.be.json;
                res.should.be.a('object')
                res.body.should.have.all.keys(
                    'reachableFromUrl','stopUrl','departuresUrl','arrivalsUrl','journeysUrl',
                    'tripUrl','nearbyUrl','locationsUrl','radarUrl','refreshJourneyUrl')
                done();
            });
    });
});