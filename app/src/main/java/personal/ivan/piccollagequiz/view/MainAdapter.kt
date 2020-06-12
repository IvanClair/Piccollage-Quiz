package personal.ivan.piccollagequiz.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.databinding.VhFontBinding

/**
 * @property listener listen item click event
 */
class MainAdapter(private val listener: View.OnClickListener) :
    ListAdapter<FontVhBindingModel, MainAdapter.MainViewHolder>(DiffCallback()) {

    // endregion

    // region Adapter Override Functions

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder = MainViewHolder(
        binding = VhFontBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        listener = listener
    )

    override fun onBindViewHolder(
        holder: MainViewHolder,
        position: Int
    ) {
        holder.bind(model = getItem(position))
    }

    // endregion

    // region View Holder

    class MainViewHolder(
        private val binding: VhFontBinding,
        private val listener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data to view
         */
        fun bind(model: FontVhBindingModel) {
            binding.apply {
                layoutMain.tag = model
                layoutMain.setOnClickListener(listener)
                textViewFontFamily.text = model.fontFamily
                textViewStyle.text = model.variantName
            }
        }
    }

    // endregion

    // region Diff Callback

    class DiffCallback : DiffUtil.ItemCallback<FontVhBindingModel>() {

        override fun areItemsTheSame(
            oldItem: FontVhBindingModel,
            newItem: FontVhBindingModel
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: FontVhBindingModel,
            newItem: FontVhBindingModel
        ): Boolean = oldItem.hashCode() == newItem.hashCode()
    }

    // endregion
}