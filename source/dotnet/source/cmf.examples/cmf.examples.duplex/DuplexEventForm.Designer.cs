namespace cmf.examples.duplex
{
    partial class DuplexEventForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.label4 = new System.Windows.Forms.Label();
            this._typeBChk = new System.Windows.Forms.CheckBox();
            this._typeAChk = new System.Windows.Forms.CheckBox();
            this.label3 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this._publishBtn = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this._eventTxt = new System.Windows.Forms.RichTextBox();
            this.label1 = new System.Windows.Forms.Label();
            this._eventTypeCbo = new System.Windows.Forms.ComboBox();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this._clearLogBtn = new System.Windows.Forms.Button();
            this._log = new System.Windows.Forms.RichTextBox();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.SuspendLayout();
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.groupBox2);
            this.splitContainer1.Panel1.Controls.Add(this.groupBox1);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.groupBox3);
            this.splitContainer1.Size = new System.Drawing.Size(658, 392);
            this.splitContainer1.SplitterDistance = 150;
            this.splitContainer1.TabIndex = 0;
            // 
            // groupBox2
            // 
            this.groupBox2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBox2.Controls.Add(this.label4);
            this.groupBox2.Controls.Add(this._typeBChk);
            this.groupBox2.Controls.Add(this._typeAChk);
            this.groupBox2.Controls.Add(this.label3);
            this.groupBox2.Location = new System.Drawing.Point(379, 12);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(267, 135);
            this.groupBox2.TabIndex = 1;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Event Subscriptions";
            // 
            // label4
            // 
            this.label4.ForeColor = System.Drawing.SystemColors.HotTrack;
            this.label4.Location = new System.Drawing.Point(25, 89);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(222, 43);
            this.label4.TabIndex = 3;
            this.label4.Text = "(You can\'t unsubscribe.  Other than this contrived example application, there are" +
    " no use cases for unsubscribing yet)";
            // 
            // _typeBChk
            // 
            this._typeBChk.AutoSize = true;
            this._typeBChk.Location = new System.Drawing.Point(28, 69);
            this._typeBChk.Name = "_typeBChk";
            this._typeBChk.Size = new System.Drawing.Size(202, 17);
            this._typeBChk.TabIndex = 2;
            this._typeBChk.Text = "cmf.examples.messages.EventTypeB";
            this._typeBChk.UseVisualStyleBackColor = true;
            this._typeBChk.CheckStateChanged += new System.EventHandler(this._typeBChk_CheckStateChanged);
            // 
            // _typeAChk
            // 
            this._typeAChk.AutoSize = true;
            this._typeAChk.Location = new System.Drawing.Point(28, 46);
            this._typeAChk.Name = "_typeAChk";
            this._typeAChk.Size = new System.Drawing.Size(202, 17);
            this._typeAChk.TabIndex = 1;
            this._typeAChk.Text = "cmf.examples.messages.EventTypeA";
            this._typeAChk.UseVisualStyleBackColor = true;
            this._typeAChk.CheckedChanged += new System.EventHandler(this._typeAChk_CheckedChanged);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(25, 22);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(222, 13);
            this.label3.TabIndex = 0;
            this.label3.Text = "Which event types would you like to receive?";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this._publishBtn);
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Controls.Add(this._eventTxt);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this._eventTypeCbo);
            this.groupBox1.Location = new System.Drawing.Point(12, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(361, 135);
            this.groupBox1.TabIndex = 0;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Event Sending";
            // 
            // _publishBtn
            // 
            this._publishBtn.Location = new System.Drawing.Point(280, 104);
            this._publishBtn.Name = "_publishBtn";
            this._publishBtn.Size = new System.Drawing.Size(75, 23);
            this._publishBtn.TabIndex = 1;
            this._publishBtn.Text = "Publish";
            this._publishBtn.UseVisualStyleBackColor = true;
            this._publishBtn.Click += new System.EventHandler(this._publishBtn_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(6, 49);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(104, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "Add some event text";
            // 
            // _eventTxt
            // 
            this._eventTxt.Location = new System.Drawing.Point(121, 46);
            this._eventTxt.Name = "_eventTxt";
            this._eventTxt.Size = new System.Drawing.Size(234, 52);
            this._eventTxt.TabIndex = 2;
            this._eventTxt.Text = "";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(6, 22);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(109, 13);
            this.label1.TabIndex = 1;
            this.label1.Text = "Which type of event?";
            // 
            // _eventTypeCbo
            // 
            this._eventTypeCbo.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this._eventTypeCbo.FormattingEnabled = true;
            this._eventTypeCbo.Items.AddRange(new object[] {
            "cmf.examples.messages.EventTypeA",
            "cmf.examples.messages.EventTypeB"});
            this._eventTypeCbo.Location = new System.Drawing.Point(121, 19);
            this._eventTypeCbo.Name = "_eventTypeCbo";
            this._eventTypeCbo.Size = new System.Drawing.Size(234, 21);
            this._eventTypeCbo.TabIndex = 0;
            // 
            // groupBox3
            // 
            this.groupBox3.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBox3.Controls.Add(this._clearLogBtn);
            this.groupBox3.Controls.Add(this._log);
            this.groupBox3.Location = new System.Drawing.Point(12, 3);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(634, 223);
            this.groupBox3.TabIndex = 0;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Received Events";
            // 
            // _clearLogBtn
            // 
            this._clearLogBtn.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this._clearLogBtn.Location = new System.Drawing.Point(553, 194);
            this._clearLogBtn.Name = "_clearLogBtn";
            this._clearLogBtn.Size = new System.Drawing.Size(75, 23);
            this._clearLogBtn.TabIndex = 1;
            this._clearLogBtn.Text = "Clear Log";
            this._clearLogBtn.UseVisualStyleBackColor = true;
            this._clearLogBtn.Click += new System.EventHandler(this._clearLogBtn_Click);
            // 
            // _log
            // 
            this._log.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this._log.Location = new System.Drawing.Point(6, 19);
            this._log.Name = "_log";
            this._log.Size = new System.Drawing.Size(622, 169);
            this._log.TabIndex = 0;
            this._log.Text = "";
            // 
            // DuplexEventForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(658, 392);
            this.Controls.Add(this.splitContainer1);
            this.Name = "DuplexEventForm";
            this.Text = ".NET Event Consumer & Producer";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.DuplexEventForm_FormClosed);
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.CheckBox _typeBChk;
        private System.Windows.Forms.CheckBox _typeAChk;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button _publishBtn;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.RichTextBox _eventTxt;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox _eventTypeCbo;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.Button _clearLogBtn;
        private System.Windows.Forms.RichTextBox _log;
    }
}

