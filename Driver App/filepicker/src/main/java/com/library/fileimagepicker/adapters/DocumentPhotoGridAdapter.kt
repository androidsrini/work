package com.library.fileimagepicker.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.library.fileimagepicker.R
import com.library.fileimagepicker.filepicker.FilePickerConst
import com.library.fileimagepicker.filepicker.PickerManager
import com.library.fileimagepicker.filepicker.utils.AndroidLifecycleUtils
import com.library.fileimagepicker.filepicker.views.SmoothCheckBox
import com.library.fileimagepicker.models.Document
import java.io.File
import java.util.*

class DocumentPhotoGridAdapter(private val context: Context,
                               document: List<Document>,
                               selectedPaths: ArrayList<String>,
                               private val mListener: FileAdapterListener?) : SelectableAdapter<DocumentPhotoGridAdapter.PhotoViewHolder, Document>(document, selectedPaths) {
    private var imageSize: Int = 0
    private var glide: RequestManager

    init {
        setColumnNumber(context, 3)
        glide = Glide.with(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_photo_layout, parent, false)

        return PhotoViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_TYPE_PHOTO
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {

            val document = items[position]

            if (AndroidLifecycleUtils.canLoadImage(holder.imageView.context)) {
                glide.load(File(document.path))
                        .apply(RequestOptions
                                .centerCropTransform()
                                .override(imageSize, imageSize)
                                .placeholder(R.drawable.image_placeholder))
                        .thumbnail(0.5f)
                        .into(holder.imageView)
            }

            holder.videoIcon.visibility = View.GONE
            /*if (document.mimeType == FilePickerConst.MEDIA_TYPE_VIDEO)
                holder.videoIcon.visibility = View.VISIBLE
            else
                holder.videoIcon.visibility = View.GONE*/

            holder.itemView.setOnClickListener { onItemClicked(holder, document) }

            //in some cases, it will prevent unwanted situations
            holder.checkBox.visibility = View.GONE
            holder.checkBox.setOnCheckedChangeListener(null)
            holder.checkBox.setOnClickListener { onItemClicked(holder, document) }

            //if true, your checkbox will be selected, else unselected
            holder.checkBox.isChecked = isSelected(document)

            holder.selectBg.visibility = if (isSelected(document)) View.VISIBLE else View.GONE
            holder.checkBox.visibility = if (isSelected(document)) View.VISIBLE else View.GONE

            holder.checkBox.setOnCheckedChangeListener(object : SmoothCheckBox.OnCheckedChangeListener {
                override fun onCheckedChanged(checkBox: SmoothCheckBox, isChecked: Boolean) {
                    toggleSelection(document)
                    holder.selectBg.visibility = if (isChecked) View.VISIBLE else View.GONE

                    if (isChecked) {
                        holder.checkBox.visibility = View.VISIBLE
                        PickerManager.add(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
                    } else {
                        holder.checkBox.visibility = View.GONE
                        PickerManager.remove(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
                    }

                    mListener?.onItemSelected()
                }
            })

        } else {
            holder.imageView.setImageResource(PickerManager.cameraDrawable)
            holder.checkBox.visibility = View.GONE
            //holder.itemView.setOnClickListener(cameraOnClickListener)
            holder.videoIcon.visibility = View.GONE
        }
    }

    private fun onItemClicked(holder: PhotoViewHolder, document: Document) {
        if (PickerManager.getMaxCount() == 1) {
            PickerManager.add(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
            mListener?.onItemSelected()
        } else if (holder.checkBox.isChecked || PickerManager.shouldAdd()) {
            holder.checkBox.setChecked(!holder.checkBox.isChecked, true)
        }
    }

    private fun setColumnNumber(context: Context, columnNum: Int) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        imageSize = widthPixels / columnNum
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var checkBox: SmoothCheckBox

        var imageView: ImageView

        var videoIcon: ImageView

        var selectBg: View

        init {
            checkBox = itemView.findViewById<View>(R.id.checkbox) as SmoothCheckBox
            imageView = itemView.findViewById<View>(R.id.iv_photo) as ImageView
            videoIcon = itemView.findViewById<View>(R.id.video_icon) as ImageView
            selectBg = itemView.findViewById(R.id.transparent_bg)
        }
    }

    companion object {
        val ITEM_TYPE_PHOTO = 101
    }
}