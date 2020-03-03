package com.example.wimmy.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.wimmy.db.MediaStore_Dao
import com.example.wimmy.R
import com.example.wimmy.db.PhotoViewModel
import com.example.wimmy.db.TagData
import com.example.wimmy.db.thumbnailData
import java.io.File

class RecyclerAdapterForder(val context: FragmentActivity?, var list: List<thumbnailData>, val itemClick: (thumbnailData) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapterForder.Holder>()
{
    private var size : Int = 200
    private var padding_size = 200

    inner class setThumbnailAsyncTask(imageView: ImageView, id : Long) : AsyncTask<Context, Void, Bitmap>() {
        private val imageView : ImageView = imageView
        private val id = id

        override fun doInBackground(vararg params: Context?): Bitmap? {
            return MediaStore_Dao.LoadThumbnail(params[0]!!, id)
        }

        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
            imageView.layoutParams.width = size
            imageView.layoutParams.height = size
        }
    }

    inner class Holder(itemView: View,  itemClick: (thumbnailData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        //thumbnail_imgview 변수 받아오기
        var thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
        var text = itemView.findViewById<TextView>(R.id.thumbnail_text)

        fun bind(data : thumbnailData) {
            //photo_view의 내부 값 설정
            val layoutParam = thumbnail.layoutParams as ViewGroup.MarginLayoutParams
            thumbnail.layoutParams.width = size
            thumbnail.layoutParams.height = size
            layoutParam.setMargins(padding_size, padding_size, padding_size, padding_size)
            setThumbnailAsyncTask(thumbnail,data.photo_id).execute(context!!.applicationContext)

            text.text = File(data.data).name

            itemView.setOnClickListener { itemClick(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.thumbnail_forderview, parent, false)


        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    fun setPhotoSize(size : Int, padding_size : Int) {
        this.size = size
        this.padding_size = padding_size
        notifyDataSetChanged()
    }

    fun setThumbnailList(list : List<thumbnailData>) {
        this.list = list
        notifyDataSetChanged()
    }
}

