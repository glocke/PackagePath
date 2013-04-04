package packagepath

import org.springframework.dao.DataIntegrityViolationException

class PackageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
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
