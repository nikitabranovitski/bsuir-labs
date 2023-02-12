package com.branovitski.arcorewithandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.ar.core.Anchor
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    private var isTracking: Boolean = false
    private var isHitting: Boolean = false

    private var anchorNode: AnchorNode? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = sceneform_fragment as ArFragment

        arFragment.arSceneView.scene.addOnUpdateListener {
            arFragment.onUpdate(it)
            onUpdate()
        }

        FAB.setOnClickListener {
            addObject(Uri.parse("model.sfb"))
        }

        clearFAB.setOnClickListener {
            removeObject(anchorNode)
        }
        showFab(false)
    }

    private fun removeObject(anchorNode: AnchorNode?) {
        if (anchorNode != null) {
            arFragment.arSceneView.scene.removeChild(anchorNode)
            anchorNode.anchor?.detach()
            anchorNode.setParent(null)
        }
    }

    private fun showFab(b: Boolean) {
        if (b) {
            FAB.isEnabled = true
            FAB.visibility = View.VISIBLE
        } else {
            FAB.isEnabled = false
            FAB.visibility = View.GONE
        }
    }

    private fun onUpdate() {
        updateTracking()

        if (isTracking) {
            val hitTestChanges = updateHitTest()
            if (hitTestChanges) {
                showFab(isHitting)
            }
        }
    }

    private fun updateHitTest(): Boolean {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()
        val hits: List<HitResult>
        val wasHitting = isHitting
        isHitting = false
        if (frame != null) {
            hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    isHitting = true
                    break
                }
            }
        }
        return wasHitting != isHitting
    }

    private fun updateTracking(): Boolean {
        val frame = arFragment.arSceneView.arFrame
        val wasTracking = isTracking
        if (frame != null) {
            isTracking = frame.camera.trackingState == TrackingState.TRACKING
        }
        return isTracking != wasTracking
    }

    private fun getScreenCenter(): Point {
        val view = findViewById<View>(android.R.id.content)
        return Point(view.width / 2, view.height / 2)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addObject(model: Uri) {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()
        if (frame != null) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    placeObject(arFragment, hit.createAnchor(), model)
                    break
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder()
            .setSource(fragment.context, model)
            .build()
            .thenApply {
                addNodeToScene(fragment, anchor, it)
            }
            .exceptionally {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                return@exceptionally
            }
    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: ModelRenderable) {
        anchorNode = AnchorNode(anchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.renderable = renderable
        transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }
}
