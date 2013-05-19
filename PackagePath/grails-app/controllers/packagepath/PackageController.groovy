package packagepath

import org.springframework.dao.DataIntegrityViolationException

class PackageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	// Brandon - take this action, put it where you want it
	// NOT COMPLETE
	def testTracker(String type, String trackingNumber) {
		Package p
		
		if (params.type.equalsIgnoreCase("ups")) {
			def upsService = new UPSService()
			p = upsService.getTrackingInfo(params.trackingNumber)
		} else {
			p = new Package()	
		}
		
		System.out.println(p.toString())
		redirect(action: "list")
	}
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
		/*def uspsService = new USPSService()
		uspsService.getTrackingInfo('1Z12345E1512345676')*/
		
        params.max = Math.min(max ?: 10, 100)
        [packageInstanceList: Package.list(params), packageInstanceTotal: Package.count()]
    }

    def create() {
        [packageInstance: new Package(params)]
    }

    def save() {
        def packageInstance = new Package(params)
        if (!packageInstance.save(flush: true)) {
            render(view: "create", model: [packageInstance: packageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'package.label', default: 'Package'), packageInstance.id])
        redirect(action: "show", id: packageInstance.id)
    }

    def show(Long id) {
        def packageInstance = Package.get(id)
        if (!packageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'package.label', default: 'Package'), id])
            redirect(action: "list")
            return
        }

        [packageInstance: packageInstance]
    }

    def edit(Long id) {
        def packageInstance = Package.get(id)
        if (!packageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'package.label', default: 'Package'), id])
            redirect(action: "list")
            return
        }

        [packageInstance: packageInstance]
    }

    def update(Long id, Long version) {
        def packageInstance = Package.get(id)
        if (!packageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'package.label', default: 'Package'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (packageInstance.version > version) {
                packageInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'package.label', default: 'Package')] as Object[],
                          "Another user has updated this Package while you were editing")
                render(view: "edit", model: [packageInstance: packageInstance])
                return
            }
        }

        packageInstance.properties = params

        if (!packageInstance.save(flush: true)) {
            render(view: "edit", model: [packageInstance: packageInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'package.label', default: 'Package'), packageInstance.id])
        redirect(action: "show", id: packageInstance.id)
    }

    def delete(Long id) {
        def packageInstance = Package.get(id)
        if (!packageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'package.label', default: 'Package'), id])
            redirect(action: "list")
            return
        }

        try {
            packageInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'package.label', default: 'Package'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'package.label', default: 'Package'), id])
            redirect(action: "show", id: id)
        }
    }
}
